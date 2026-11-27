package com.youbili.modules.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youbili.common.exception.ServiceException;
import com.youbili.common.response.ServiceCode;
import com.youbili.modules.auth.service.AuthService;
import com.youbili.modules.interaction.mapper.FavoriteMapper;
import com.youbili.modules.interaction.mapper.HistoryMapper;
import com.youbili.modules.user.dto.UpdateProfileRequest;
import com.youbili.modules.user.entity.Follow;
import com.youbili.modules.user.entity.User;
import com.youbili.modules.user.mapper.FollowMapper;
import com.youbili.modules.user.mapper.UserMapper;
import com.youbili.modules.user.vo.UserProfileVO;
import com.youbili.modules.user.vo.UserSpaceVO;
import com.youbili.modules.video.mapper.VideoMapper;
import com.youbili.modules.video.vo.VideoCardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final FollowMapper followMapper;
    private final VideoMapper videoMapper;
    private final FavoriteMapper favoriteMapper;
    private final HistoryMapper historyMapper;

    public UserSpaceVO getUserSpace(Long targetUserId, Long currentUserId) {
        User user = userMapper.selectById(targetUserId);
        if (user == null) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "用户不存在");
        }
        boolean isSelf = Objects.equals(targetUserId, currentUserId);
        boolean isFollowing = false;
        if (!isSelf && currentUserId != null) {
            isFollowing = followMapper.exists(
                    new LambdaQueryWrapper<Follow>()
                            .eq(Follow::getFollowerId, currentUserId)
                            .eq(Follow::getFollowedId, targetUserId)
                            .eq(Follow::getEnable, 1)
            );
        }

        List<VideoCardVO> videos = videoMapper
                .selectUserVideos(new Page<>(1, 12), targetUserId, null, !isSelf)
                .getRecords();
        List<VideoCardVO> favorites = favoriteMapper.selectRecentFavoriteCards(targetUserId, 12);
        List<VideoCardVO> histories = isSelf
                ? historyMapper.selectRecentHistoryCards(targetUserId, 12)
                : Collections.emptyList();

        UserProfileVO profileVO = AuthService.toProfileVO(user);
        if (!isSelf) {
            // 隐藏他人邮箱隐私
            profileVO.setEmail(null);
        }

        return UserSpaceVO.builder()
                .profile(profileVO)
                .isSelf(isSelf)
                .isFollowing(isFollowing)
                .videos(videos)
                .favorites(favorites)
                .histories(histories)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public UserProfileVO updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "用户不存在");
        }
        if (StringUtils.hasText(request.getNickname())) {
            user.setNickname(request.getNickname().trim());
        }
        if (request.getBirthday() != null) {
            user.setBirthday(request.getBirthday());
        }
        if (StringUtils.hasText(request.getAvatarUrl())) {
            user.setImgUrl(request.getAvatarUrl().trim());
        }
        userMapper.updateById(user);
        return AuthService.toProfileVO(user);
    }
}
