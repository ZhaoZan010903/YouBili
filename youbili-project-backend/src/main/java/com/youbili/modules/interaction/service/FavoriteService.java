package com.youbili.modules.interaction.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.youbili.common.exception.ServiceException;
import com.youbili.common.response.ServiceCode;
import com.youbili.modules.interaction.entity.Favorite;
import com.youbili.modules.interaction.mapper.FavoriteMapper;
import com.youbili.modules.interaction.vo.FavoriteItemVO;
import com.youbili.modules.interaction.vo.ReactionVO;
import com.youbili.modules.video.entity.Video;
import com.youbili.modules.video.mapper.VideoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final VideoMapper videoMapper;
    private final LikeService likeService;

    public List<FavoriteItemVO> listFavorites(Long userId, String keyword) {
        return favoriteMapper.selectFavoriteList(userId, keyword == null ? null : keyword.trim());
    }

    @Transactional(rollbackFor = Exception.class)
    public ReactionVO addFavorite(Long userId, Long videoId) {
        Video video = videoMapper.selectById(videoId);
        if (video == null || !Integer.valueOf(1).equals(video.getEnable())) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "视频不存在");
        }
        Favorite existing = favoriteMapper.selectOne(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .eq(Favorite::getVideoId, videoId)
        );
        if (existing == null) {
            Favorite fav = new Favorite();
            fav.setUserId(userId);
            fav.setVideoId(videoId);
            fav.setFavoriteDate(LocalDateTime.now());
            fav.setEnable(1);
            favoriteMapper.insert(fav);
            videoMapper.adjustFavoriteCount(videoId, 1);
        } else if (!Integer.valueOf(1).equals(existing.getEnable())) {
            existing.setEnable(1);
            existing.setFavoriteDate(LocalDateTime.now());
            favoriteMapper.updateById(existing);
            videoMapper.adjustFavoriteCount(videoId, 1);
        }
        return likeService.getReaction(userId, videoId);
    }

    @Transactional(rollbackFor = Exception.class)
    public ReactionVO removeFavoriteByVideo(Long userId, Long videoId) {
        Favorite existing = favoriteMapper.selectOne(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .eq(Favorite::getVideoId, videoId)
                        .eq(Favorite::getEnable, 1)
        );
        if (existing != null) {
            existing.setEnable(0);
            favoriteMapper.updateById(existing);
            videoMapper.adjustFavoriteCount(videoId, -1);
        }
        return likeService.getReaction(userId, videoId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeFavoriteById(Long userId, Long favoriteId) {
        Favorite existing = favoriteMapper.selectById(favoriteId);
        if (existing == null || !Integer.valueOf(1).equals(existing.getEnable())) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "收藏记录不存在");
        }
        if (!Objects.equals(existing.getUserId(), userId)) {
            throw new ServiceException(ServiceCode.FORBIDDEN, "无权删除他人收藏");
        }
        existing.setEnable(0);
        favoriteMapper.updateById(existing);
        videoMapper.adjustFavoriteCount(existing.getVideoId(), -1);
    }
}
