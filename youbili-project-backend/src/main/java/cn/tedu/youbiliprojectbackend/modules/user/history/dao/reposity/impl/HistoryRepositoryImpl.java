package cn.tedu.youbiliprojectbackend.modules.user.history.dao.reposity.impl;

import cn.tedu.youbiliprojectbackend.modules.user.history.dao.mapper.HistoryMapper;
import cn.tedu.youbiliprojectbackend.modules.user.history.dao.reposity.IHistoryRepository;
import cn.tedu.youbiliprojectbackend.modules.user.history.pojo.catche.HistoryStandardCatch;
import cn.tedu.youbiliprojectbackend.modules.user.history.pojo.vo.HistoryListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 处理历史记录数据的存储库实现类
 *
 * @author 杨影茹
 * @version 1.0
 */
@Slf4j
@Repository
public class HistoryRepositoryImpl implements IHistoryRepository {

    @Autowired
    private HistoryMapper historyMapper;

    public HistoryRepositoryImpl() {
        log.info("创建存储库对象：HistoryRepositoryImpl");
    }

    // 根据userId,historyId删除历史记录数据
    @Override
    public int deleteByHistoryId(Long userId, Long historyId) {
        log.debug("开始执行【historyId删除历史记录】的数据访问，用户Id：{},历史记录Id: {}", userId, historyId);
        return historyMapper.deleteByHistoryId(userId, historyId);
    }


    @Override
    public List<HistoryListVO> list(Long userId) {
        log.debug("开始执行【查询历史记录列表】的数据访问");
        List<HistoryListVO> list = historyMapper.listByUserId(userId);
        return list;
    }

    @Override
    public HistoryStandardCatch selectHistory(Long userID, Long videoID) {
        log.debug("开始执行【查询历史记录】的数据访问,用户Id：{},视频Id: {}", userID, videoID);
        HistoryStandardCatch historyStandardVO = historyMapper.selectHistory(userID, videoID);
        return historyStandardVO;
    }

    @Override
    public void updateEnable(Long userID, Long videoID) {
        log.debug("开始执行【启用历史记录】的数据访问,用户Id：{},视频Id: {}", userID, videoID);
        historyMapper.updateEnable(userID, videoID);
    }

    @Override
    public int updateWatchDuration(Long userID, Long videoID, LocalDateTime watchDuration) {
        log.debug("开始执行【更新时间戳】的数据访问，用户Id：{},视频Id: {},时间戳：{}", userID, videoID, watchDuration);
        return historyMapper.updateWatchDuration(userID, videoID, watchDuration);
    }

    @Override
    public int updateWatchDate(Long userID, Long videoID, LocalDateTime watchDate) {
        return historyMapper.updateWatchDate(userID, videoID, watchDate);
    }

    @Override
    public int add(Long userID, Long videoID, LocalDateTime watchDuration, LocalDateTime watchDate) {
        log.debug("开始执行【更新观看日期】的数据访问，用户Id：{},视频Id: {}", userID, videoID);
        return historyMapper.add(userID, videoID, watchDuration, watchDate);
    }

}