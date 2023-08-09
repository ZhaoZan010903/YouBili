package cn.tedu.youbiliprojectbackend.modules.user.history.dao.mapper;

import cn.tedu.youbiliprojectbackend.modules.user.history.pojo.catche.HistoryStandardCatch;
import cn.tedu.youbiliprojectbackend.modules.user.history.pojo.vo.HistoryListVO;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 处理历史记录数据的Mapper接口
 *
 * @author 杨影茹
 * @version 1.0
 */
@Repository
public interface HistoryMapper {

    /**
     * 根据用户Id查询用户历史记录列表
     *
     * @param userID 用户ID
     * @return 返回的历史记录列表数据
     */
    List<HistoryListVO> listByUserId(Long userID);

    /**
     * 根据historyId删除历史记录数据
     *
     * @param historyID 历史记录数据ID
     * @return 受影响的行数
     */
    int deleteByHistoryId(Long userID, Long historyID);


    /**
     * 根据userID,videoID查询历史记录是否存在
     *
     * @param userID  用户ID
     * @param videoID 视频ID
     * @return 匹配的历史记录，如果没有匹配的数据，则返回null
     */
    HistoryStandardCatch selectHistory(Long userID, Long videoID);


    /**
     * 根据userID,videoID启用历史记录
     *
     * @param userID  用户ID
     * @param videoID 视频Id
     */
    void updateEnable(Long userID, Long videoID);


    /**
     * 根据userID,videoID更改时间戳
     *
     * @param userID        用户ID
     * @param videoID       视频Id
     * @param watchDuration 时间戳
     * @return 受影响的行数
     */
    int updateWatchDuration(Long userID, Long videoID, LocalDateTime watchDuration);

    /**
     * 根据userID,videoID更改观看时间
     *
     * @param userID    用户ID
     * @param videoID   视频Id
     * @param watchDate 观看时间
     * @return 受影响的行数
     */
    int updateWatchDate(Long userID, Long videoID, LocalDateTime watchDate);

    /**
     * 根据userID,videoID,watchDuration 添加历史记录
     *
     * @param userID        用户ID
     * @param videoID       视频Id
     * @param watchDuration 时间戳
     * @param watchDate     时间戳
     * @return 受影响的行数
     */
    int add(Long userID, Long videoID, LocalDateTime watchDuration, LocalDateTime watchDate);

}
