package cn.tedu.youbiliprojectbackend.modules.user.history.dao.cache;

import java.time.LocalDateTime;

/**
 * 从缓存之中添加历史记录的数据信息
 *
 * @author 杨影茹
 */
public interface HistorySelect {
    /**
     * 根据userID,videoID添加历史记录
     *
     * @param userID  用户ID
     * @param videoID 视频Id
     * @return 匹配的历史记录，如果没有匹配的数据，则返回null
     */
    void insert(Long userID, Long videoID, LocalDateTime watchDuration);

}

