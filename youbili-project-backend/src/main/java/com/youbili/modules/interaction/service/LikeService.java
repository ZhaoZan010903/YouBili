package com.youbili.modules.interaction.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.youbili.common.exception.ServiceException;
import com.youbili.common.response.ServiceCode;
import com.youbili.modules.interaction.entity.Favorite;
import com.youbili.modules.interaction.entity.VideoLike;
import com.youbili.modules.interaction.mapper.FavoriteMapper;
import com.youbili.modules.interaction.mapper.LikeMapper;
import com.youbili.modules.interaction.vo.ReactionVO;
import com.youbili.modules.user.mapper.UserMapper;
import com.youbili.modules.video.entity.Video;
import com.youbili.modules.video.mapper.VideoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeMapper likeMapper;
    private final FavoriteMapper favoriteMapper;
    private final VideoMapper videoMapper;
    private final UserMapper userMapper;

    public ReactionVO getReaction(Long userId, Long videoId) {
        Video video = requireVideo(videoId);
        VideoLike existing = findActive(userId, videoId);
        boolean liked = existing != null && Integer.valueOf(1).equals(existing.getTypeEnable());
        boolean disliked = existing != null && Integer.valueOf(0).equals(existing.getTypeEnable());
        boolean favorited = favoriteMapper.exists(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .eq(Favorite::getVideoId, videoId)
                        .eq(Favorite::getEnable, 1)
        );
        return ReactionVO.builder()
                .liked(liked)
                .disliked(disliked)
                .favorited(favorited)
                .likeCount(safe(video.getLikeCount()))
                .dislikeCount(safe(video.getDislikeCount()))
                .favoriteCount(safe(video.getFavoriteCount()))
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public ReactionVO like(Long userId, Long videoId) {
        Video video = requireVideo(videoId);
        VideoLike existing = findAny(userId, videoId);
        if (existing == null) {
            VideoLike record = new VideoLike();
            record.setUserId(userId);
            record.setVideoId(videoId);
            record.setTypeEnable(1);
            record.setEnable(1);
            record.setLikeDate(LocalDateTime.now());
            likeMapper.insert(record);
            videoMapper.adjustLikeCount(videoId, 1);
            userMapper.adjustTotalLikes(video.getUserId(), 1);
        } else if (!Integer.valueOf(1).equals(existing.getEnable())) {
            existing.setEnable(1);
            existing.setTypeEnable(1);
            existing.setLikeDate(LocalDateTime.now());
            likeMapper.updateById(existing);
            videoMapper.adjustLikeCount(videoId, 1);
            userMapper.adjustTotalLikes(video.getUserId(), 1);
        } else if (Integer.valueOf(0).equals(existing.getTypeEnable())) {
            // 从点踩无缝切换为点赞
            existing.setTypeEnable(1);
            existing.setLikeDate(LocalDateTime.now());
            likeMapper.updateById(existing);
            videoMapper.adjustDislikeCount(videoId, -1);
            videoMapper.adjustLikeCount(videoId, 1);
            userMapper.adjustTotalLikes(video.getUserId(), 1);
        }
        return getReaction(userId, videoId);
    }

    @Transactional(rollbackFor = Exception.class)
    public ReactionVO cancelLike(Long userId, Long videoId) {
        Video video = requireVideo(videoId);
        VideoLike existing = findActive(userId, videoId);
        if (existing != null && Integer.valueOf(1).equals(existing.getTypeEnable())) {
            existing.setEnable(0);
            likeMapper.updateById(existing);
            videoMapper.adjustLikeCount(videoId, -1);
            userMapper.adjustTotalLikes(video.getUserId(), -1);
        }
        return getReaction(userId, videoId);
    }

    @Transactional(rollbackFor = Exception.class)
    public ReactionVO dislike(Long userId, Long videoId) {
        Video video = requireVideo(videoId);
        VideoLike existing = findAny(userId, videoId);
        if (existing == null) {
            VideoLike record = new VideoLike();
            record.setUserId(userId);
            record.setVideoId(videoId);
            record.setTypeEnable(0);
            record.setEnable(1);
            record.setLikeDate(LocalDateTime.now());
            likeMapper.insert(record);
            videoMapper.adjustDislikeCount(videoId, 1);
        } else if (!Integer.valueOf(1).equals(existing.getEnable())) {
            existing.setEnable(1);
            existing.setTypeEnable(0);
            existing.setLikeDate(LocalDateTime.now());
            likeMapper.updateById(existing);
            videoMapper.adjustDislikeCount(videoId, 1);
        } else if (Integer.valueOf(1).equals(existing.getTypeEnable())) {
            // 从点赞无缝切换为点踩
            existing.setTypeEnable(0);
            existing.setLikeDate(LocalDateTime.now());
            likeMapper.updateById(existing);
            videoMapper.adjustLikeCount(videoId, -1);
            userMapper.adjustTotalLikes(video.getUserId(), -1);
            videoMapper.adjustDislikeCount(videoId, 1);
        }
        return getReaction(userId, videoId);
    }

    @Transactional(rollbackFor = Exception.class)
    public ReactionVO cancelDislike(Long userId, Long videoId) {
        requireVideo(videoId);
        VideoLike existing = findActive(userId, videoId);
        if (existing != null && Integer.valueOf(0).equals(existing.getTypeEnable())) {
            existing.setEnable(0);
            likeMapper.updateById(existing);
            videoMapper.adjustDislikeCount(videoId, -1);
        }
        return getReaction(userId, videoId);
    }

    private Video requireVideo(Long videoId) {
        Video video = videoMapper.selectById(videoId);
        if (video == null || !Integer.valueOf(1).equals(video.getEnable())) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "视频不存在");
        }
        return video;
    }

    private VideoLike findAny(Long userId, Long videoId) {
        return likeMapper.selectOne(
                new LambdaQueryWrapper<VideoLike>()
                        .eq(VideoLike::getUserId, userId)
                        .eq(VideoLike::getVideoId, videoId)
        );
    }

    private VideoLike findActive(Long userId, Long videoId) {
        return likeMapper.selectOne(
                new LambdaQueryWrapper<VideoLike>()
                        .eq(VideoLike::getUserId, userId)
                        .eq(VideoLike::getVideoId, videoId)
                        .eq(VideoLike::getEnable, 1)
        );
    }

    private int safe(Integer value) {
        return value == null ? 0 : value;
    }
}
