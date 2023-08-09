package cn.tedu.youbiliprojectbackend.modules.user.follow.dao.persist.repository.impl;

import cn.tedu.youbiliprojectbackend.modules.user.follow.dao.persist.mapper.FollowMapper;
import cn.tedu.youbiliprojectbackend.modules.user.follow.dao.persist.repository.IFollowRepository;
import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.entity.followedID;
import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo.FollowListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class FollowRepositoryImpl implements IFollowRepository {
    @Autowired
    FollowMapper followMapper;

    /**
     * 用户关注列表
     *
     * @param userID 当事人ID
     * @return 返回用户关注人列表
     */
    @Override
    public List<FollowListVO> selectListByUserId(Long userID) {
        return followMapper.selectListByUserId(userID);
    }

    /**
     * 添加关注
     *
     * @param followID 关注实体
     * @return
     */
    @Override
    public int addFollow(Long followerID, Long followedID) {
        return followMapper.insertAll(followerID, followedID);
    }

    /**
     * 取消关注
     *
     * @param userID   当事人ID
     * @param followID
     * @return
     */
    @Override
    public int delete(Long userID, Long followID) {
        return followMapper.delete(userID, followID);
    }

    /**
     * @param followerID 来自当事人ID
     * @param followedID 被关注人ID
     * @return
     */
    @Override
    public int selectByFollowerIDAndFollowedID(Long followerID, Long followedID) {
        return followMapper.selectByFollowerIDAndFollowedID(followerID, followedID);
    }

    @Override
    public followedID selectByUserIDAndFollowID(Long userID, Long followID) {
        return followMapper.selectByUserIDAndFollowID(userID, followID);
    }

    @Override
    public Long selectByFollowIdRes(Long followID) {
        return followMapper.selectByFollowId(followID);
    }
}
