package cn.tedu.youbiliprojectbackend.modules.user.follow.service.impl;

import cn.tedu.youbiliprojectbackend.common.ex.ServiceException;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.user.follow.dao.persist.repository.IFollowRepository;
import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.entity.followedID;
import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo.FollowListVO;
import cn.tedu.youbiliprojectbackend.modules.user.follow.service.IFollowService;
import cn.tedu.youbiliprojectbackend.modules.user.usercount.dao.cache.UserCountCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FollowServiceImpl implements IFollowService {
    @Autowired
    IFollowRepository followRepository;

    @Autowired
    UserCountCacheRepository userCountCacheRepository;

    /**
     * 查看关注列表
     *
     * @param userID 关注人的ID
     * @return 返回被关注人的列表
     */
    @Override
    public List<FollowListVO> list(Long userID) {
        List<FollowListVO> list = followRepository.selectListByUserId(userID);

//        if(list == null) {
//            throw new ServiceException(ServiceCode.ERROR_CONFLICT,"没有查询到响应结果");
//        }

        return list;
    }

    /**
     * 添加关注
     *
     * @param followerID 关注人的ID
     * @param followedID 被关注人(粉丝)的ID
     * @return
     */
    @Override
    public void addFollow(Long followerID, Long followedID) {

        int row = followRepository.selectByFollowerIDAndFollowedID(followerID, followedID);
        if (row > 0) {
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, "您已关注了当前用户!");
        }
        System.out.println("111" + followedID + followedID);
        followRepository.addFollow(followerID, followedID);
        userCountCacheRepository.addFollow(followerID);
        userCountCacheRepository.addFans(followedID);

    }


    /**
     * followID + 当事人ID来删
     * 首先明确一点 ,只有我 才能删我的关注 !而不是 我能删别人的关注
     *
     * @param userID   当前用户的ID
     * @param followID 关注表的ID
     */
    @Override
    public void delete(Long userID, Long followID) {
        followedID followedIDs = followRepository.selectByUserIDAndFollowID(userID, followID);
        log.debug(String.valueOf(followedIDs));
        if (followedIDs == null) {
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, "您并未关注当前用户,不可进行取消关注操作!");
        }
        followRepository.delete(userID, followID);
        userCountCacheRepository.removeFollow(userID);
        userCountCacheRepository.removeFans(followedIDs.getFollowedID());

    }

    @Override
    public Long selectByFollowIdSer(Long followID) {
        return followRepository.selectByFollowIdRes(followID);
    }


}
