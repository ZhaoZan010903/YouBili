package cn.tedu.youbiliprojectbackend.modules.user.history.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.common.consts.HistoryConsts;
import cn.tedu.youbiliprojectbackend.modules.user.history.dao.cache.HistorySelect;
import cn.tedu.youbiliprojectbackend.modules.user.history.pojo.catche.HistoryCatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author 杨影茹
 */
@Repository
@Slf4j
public class HistorySelectImpl implements HistorySelect, HistoryConsts {

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public void insert(Long userID, Long videoID, LocalDateTime watchDuration) {
        String key = HISTORY_DATA + userID + ":" + videoID;
        HistoryCatch newHistoryCatch = (HistoryCatch) redisTemplate.opsForValue().get(key);

        if (newHistoryCatch == null || newHistoryCatch.getEnable() == null) {
            // 如果 Redis 中没有该历史记录对象，执行插入操作
            newHistoryCatch = new HistoryCatch();
            newHistoryCatch.setUserID(userID);
            newHistoryCatch.setVideoID(videoID);
            newHistoryCatch.setEnable(1);
            log.debug(newHistoryCatch.getWatchDuration());
        } else {
            if (newHistoryCatch.getEnable() == 0) {
                // 更新历史记录对象的启用状态字段
                newHistoryCatch.setEnable(1);
            }
        }
        // 插入/更新获取的观看日期和时间戳
        newHistoryCatch.setWatchDate(LocalDateTime.now().toString());
        newHistoryCatch.setWatchDuration(watchDuration.toString());
        // 将数据插入/重新存储到 Redis 中
        redisTemplate.opsForValue().set(key, newHistoryCatch);
    }

}
