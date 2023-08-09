package cn.tedu.youbiliprojectbackend.modules.user.history.service.impl;

import cn.tedu.youbiliprojectbackend.common.ex.ServiceException;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.user.history.dao.cache.HistorySelect;
import cn.tedu.youbiliprojectbackend.modules.user.history.dao.reposity.IHistoryRepository;
import cn.tedu.youbiliprojectbackend.modules.user.history.pojo.catche.HistoryCatch;
import cn.tedu.youbiliprojectbackend.modules.user.history.pojo.catche.HistoryStandardCatch;
import cn.tedu.youbiliprojectbackend.modules.user.history.pojo.vo.HistoryListVO;
import cn.tedu.youbiliprojectbackend.modules.user.history.service.IHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static cn.tedu.youbiliprojectbackend.common.consts.HistoryConsts.HISTORY_DATA;

/**
 * 处理历史记录数据的业务实现类
 *
 * @author 杨影茹
 * @version 1.0
 */
@Slf4j
@Service
public class HistoryServiceImpl implements IHistoryService {

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;
    @Autowired
    private IHistoryRepository repository;
    @Autowired
    private HistorySelect historySelect;

    public HistoryServiceImpl() {
        log.debug("创建业务类对象：HistoryServiceImpl");
    }

    @Override
    public void deleteByHistoryId(Long userID, Long historyID) {
        log.debug("开始处理【根据historyID删除历史记录】的业务，用户Id：{},历史记录Id: {}", userID, historyID);
        int queryResult = repository.deleteByHistoryId(userID, historyID);

        if (historyID == null) {
            // 是：数据不存在，抛出异常
            String message = "删除历史记录失败，尝试删除的历史记录数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        if (queryResult != 1) {
            String message = "删除历史记录失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.FAILER, message);
        }

    }

    @Override
    public List<HistoryListVO> list(Long userID) {
        log.debug("开始处理【查询历史记录列表】的业务，用户Id：{}", userID);
        if (userID == null) {
            String message = "查询历史记录列表失败，用户未登录！";
            log.warn(message);
        }
        List<HistoryListVO> list = repository.list(userID);
        return list;
    }

    public void insert(Long userID, Long videoID, LocalDateTime watchDuration) {
        log.debug("开始处理【向缓存中添加历史记录】的业务，用户Id：{},视频Id: {},时间戳: {}", userID, videoID, watchDuration);
        historySelect.insert(userID, videoID, watchDuration);
    }

    @Override
    public void updateEnable(Long userID, Long videoID) {
        repository.updateEnable(userID, videoID);
    }

    @Override
    public int updateWatchDuration(Long userID, Long videoID, LocalDateTime watchDuration) {
        return repository.updateWatchDuration(userID, videoID, watchDuration);
    }

    @Override
    public int updateWatchDate(Long userID, Long videoID, LocalDateTime watchDate) {
        return repository.updateWatchDate(userID, videoID, watchDate);
    }


    @Override
    public void add(Long userID, Long videoID, LocalDateTime watchDuration, LocalDateTime watchDate) {
        log.debug("开始处理【向数据库中添加历史记录】的业务，用户Id：{},视频Id: {},时间戳: {}", userID, videoID, watchDuration);

        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        Set<String> keys = redisTemplate.keys(HISTORY_DATA + "*:*");
        for (String key : keys) {
            HistoryCatch historyCatch = (HistoryCatch) operations.get(key);
            log.debug("historyCatch:{}", historyCatch);
            HistoryStandardCatch historyStandardCatch = repository.selectHistory(historyCatch.getUserID(), historyCatch.getVideoID());
            if (historyStandardCatch == null) {
                log.debug("没有数据 进行插入");
                repository.add(historyCatch.getUserID(), historyCatch.getVideoID(), LocalDateTime.parse(historyCatch.getWatchDuration()), LocalDateTime.parse(historyCatch.getWatchDate()));

            } else {
                log.debug("有数据进行更新");
                repository.updateWatchDuration(historyCatch.getUserID(), historyCatch.getVideoID(), LocalDateTime.parse(historyCatch.getWatchDuration()));
                repository.updateWatchDate(historyCatch.getUserID(), historyCatch.getVideoID(), LocalDateTime.parse(historyCatch.getWatchDate()));
                repository.updateEnable(historyCatch.getUserID(), historyCatch.getVideoID());
            }
            redisTemplate.delete(key);
        }

    }
}