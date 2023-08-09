package cn.tedu.youbiliprojectbackend.modules.user.follow.dao.persist.repository;

import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.entity.followedID;
import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo.FollowListVO;

import java.util.List;

public interface IFollowRepository {
    /**
     * 查询用户关注列表
     *
     * @param userID 当事人ID
     * @return 返回用户关注人列表
     */
    List<FollowListVO> selectListByUserId(Long userID);

    /**
     * 添加关注
     *
     * @param follow
     * @return
     */
    int addFollow(Long followerID, Long followedID);

    /**
     * 取消关注
     *
     * @param userID   当事人ID
     * @param followID 关注表的ID
     * @return
     */
    int delete(Long userID, Long followID);

    /**
     * 查询当事人是否已经关注了被关注人
     *
     * @param followerID 来自当事人ID
     * @param followedID 被关注人ID
     * @return
     */
    int selectByFollowerIDAndFollowedID(Long followerID, Long followedID);

    /**
     * 查询当事人是否已经关乎了被关注人
     *
     * @param userID   当事人ID
     * @param followID 关注表ID
     * @return
     */
    followedID selectByUserIDAndFollowID(Long userID, Long followID);

    Long selectByFollowIdRes(Long followID);
}
