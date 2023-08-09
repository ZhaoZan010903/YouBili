package cn.tedu.youbiliprojectbackend.modules.user.follow.service;

import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo.FollowListVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IFollowService {
    /**
     * 添加关注
     *
     * @param userID 关注人的ID
     * @return
     */
    List<FollowListVO> list(Long userID);

    /**
     * 添加关注
     *
     * @param followerID 当事人ID
     * @param followedID 被关注人ID
     */
    void addFollow(Long followerID, Long followedID);

    /**
     * 取消关注
     *
     * @param userID   当事人ID
     * @param followID 关注表的ID
     */
    void delete(Long userID, Long followID);

    Long selectByFollowIdSer(Long followID);
}
