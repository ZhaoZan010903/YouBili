package com.youbili.modules.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.youbili.common.exception.ServiceException;
import com.youbili.common.response.ServiceCode;
import com.youbili.modules.user.entity.Follow;
import com.youbili.modules.user.mapper.FollowMapper;
import com.youbili.modules.user.mapper.UserMapper;
import com.youbili.modules.user.vo.FollowUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowMapper followMapper;
    private final UserMapper userMapper;

    public List<FollowUserVO> listFollows(Long userId) {
        return followMapper.selectFollowList(userId);
    }

    public List<FollowUserVO> listFans(Long userId) {
        return followMapper.selectFanList(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void follow(Long followerId, Long targetUserId) {
        if (Objects.equals(followerId, targetUserId)) {
            throw new ServiceException(ServiceCode.BAD_REQUEST, "不能关注自己");
        }
        if (userMapper.selectById(targetUserId) == null) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "目标用户不存在");
        }

        Follow existing = followMapper.selectOne(
                new LambdaQueryWrapper<Follow>()
                        .eq(Follow::getFollowerId, followerId)
                        .eq(Follow::getFollowedId, targetUserId)
        );
        if (existing != null) {
            if (Integer.valueOf(1).equals(existing.getEnable())) {
                throw new ServiceException(ServiceCode.CONFLICT, "您已经关注了该用户");
            }
            existing.setEnable(1);
            existing.setFollowDate(LocalDateTime.now());
            followMapper.updateById(existing);
        } else {
            Follow follow = new Follow();
            follow.setFollowerId(followerId);
            follow.setFollowedId(targetUserId);
            follow.setFollowDate(LocalDateTime.now());
            follow.setEnable(1);
            followMapper.insert(follow);
        }

        userMapper.adjustFollows(followerId, 1);
        userMapper.adjustFans(targetUserId, 1);
    }

    @Transactional(rollbackFor = Exception.class)
    public void unfollow(Long followerId, Long targetUserId) {
        Follow existing = followMapper.selectOne(
                new LambdaQueryWrapper<Follow>()
                        .eq(Follow::getFollowerId, followerId)
                        .eq(Follow::getFollowedId, targetUserId)
                        .eq(Follow::getEnable, 1)
        );
        if (existing == null) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "您尚未关注该用户");
        }
        existing.setEnable(0);
        followMapper.updateById(existing);
        userMapper.adjustFollows(followerId, -1);
        userMapper.adjustFans(targetUserId, -1);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeFan(Long currentUserId, Long followerId) {
        Follow existing = followMapper.selectOne(
                new LambdaQueryWrapper<Follow>()
                        .eq(Follow::getFollowerId, followerId)
                        .eq(Follow::getFollowedId, currentUserId)
                        .eq(Follow::getEnable, 1)
        );
        if (existing == null) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "该粉丝关系不存在");
        }
        existing.setEnable(0);
        followMapper.updateById(existing);
        userMapper.adjustFollows(followerId, -1);
        userMapper.adjustFans(currentUserId, -1);
    }
}
