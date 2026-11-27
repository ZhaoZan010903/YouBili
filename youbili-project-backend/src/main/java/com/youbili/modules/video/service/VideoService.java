package com.youbili.modules.video.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youbili.common.exception.ServiceException;
import com.youbili.common.response.PageResult;
import com.youbili.common.response.ServiceCode;
import com.youbili.config.StorageProperties;
import com.youbili.modules.category.entity.Tag;
import com.youbili.modules.category.entity.VideoCategory;
import com.youbili.modules.category.entity.VideoTag;
import com.youbili.modules.category.mapper.TagMapper;
import com.youbili.modules.category.mapper.VideoCategoryMapper;
import com.youbili.modules.category.mapper.VideoTagMapper;
import com.youbili.modules.interaction.entity.Favorite;
import com.youbili.modules.interaction.entity.VideoLike;
import com.youbili.modules.interaction.mapper.FavoriteMapper;
import com.youbili.modules.interaction.mapper.LikeMapper;
import com.youbili.modules.search.service.VideoSearchService;
import com.youbili.modules.user.entity.Follow;
import com.youbili.modules.user.entity.User;
import com.youbili.modules.user.mapper.FollowMapper;
import com.youbili.modules.user.mapper.UserMapper;
import com.youbili.modules.video.dto.PublishVideoRequest;
import com.youbili.modules.video.dto.UpdateVideoRequest;
import com.youbili.modules.video.entity.Video;
import com.youbili.modules.video.mapper.VideoMapper;
import com.youbili.modules.video.vo.VideoCardVO;
import com.youbili.modules.video.vo.VideoDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoMapper videoMapper;
    private final UserMapper userMapper;
    private final TagMapper tagMapper;
    private final VideoCategoryMapper videoCategoryMapper;
    private final VideoTagMapper videoTagMapper;
    private final LikeMapper likeMapper;
    private final FavoriteMapper favoriteMapper;
    private final FollowMapper followMapper;
    private final VideoTranscodeService videoTranscodeService;
    private final StorageProperties storageProperties;
    private final VideoSearchService videoSearchService;

    public VideoDetailVO getVideoDetail(Long videoId, Long currentUserId) {
        VideoCardVO card = videoMapper.selectVideoCardById(videoId);
        if (card == null) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "视频不存在或已被删除");
        }
        Video raw = videoMapper.selectById(videoId);
        User author = userMapper.selectById(card.getUserId());
        List<Tag> tags = tagMapper.selectTagsByVideoId(videoId);

        boolean liked = false;
        boolean disliked = false;
        boolean favorited = false;
        boolean followingAuthor = false;

        if (currentUserId != null) {
            VideoLike like = likeMapper.selectOne(
                    new LambdaQueryWrapper<VideoLike>()
                            .eq(VideoLike::getUserId, currentUserId)
                            .eq(VideoLike::getVideoId, videoId)
                            .eq(VideoLike::getEnable, 1)
            );
            if (like != null) {
                liked = Integer.valueOf(1).equals(like.getTypeEnable());
                disliked = Integer.valueOf(0).equals(like.getTypeEnable());
            }
            favorited = favoriteMapper.exists(
                    new LambdaQueryWrapper<Favorite>()
                            .eq(Favorite::getUserId, currentUserId)
                            .eq(Favorite::getVideoId, videoId)
                            .eq(Favorite::getEnable, 1)
            );
            if (!Objects.equals(currentUserId, card.getUserId())) {
                followingAuthor = followMapper.exists(
                        new LambdaQueryWrapper<Follow>()
                                .eq(Follow::getFollowerId, currentUserId)
                                .eq(Follow::getFollowedId, card.getUserId())
                                .eq(Follow::getEnable, 1)
                );
            }
        }

        List<VideoDetailVO.QualityOptionVO> qualities = buildQualityOptions(card.getVideoSrcUrl());

        return VideoDetailVO.builder()
                .videoId(card.getVideoId())
                .userId(card.getUserId())
                .title(card.getTitle())
                .description(card.getDescription())
                .duration(card.getDuration())
                .uploadDate(card.getUploadDate())
                .videoSrcUrl(qualities.isEmpty() ? card.getVideoSrcUrl() : qualities.get(0).getUrl())
                .videoImgUrl(card.getVideoImgUrl())
                .authorNickname(card.getAuthorNickname())
                .authorAvatarUrl(card.getAuthorAvatarUrl())
                .authorFans(author != null && author.getFans() != null ? author.getFans() : 0)
                .authorFollows(author != null && author.getFollows() != null ? author.getFollows() : 0)
                .categoryId(card.getCategoryId())
                .categoryName(card.getCategoryName())
                .viewCount(safeInt(card.getViewCount()))
                .likeCount(safeInt(card.getLikeCount()))
                .dislikeCount(raw != null ? safeInt(raw.getDislikeCount()) : 0)
                .favoriteCount(safeInt(card.getFavoriteCount()))
                .barrageCount(safeInt(card.getBarrageCount()))
                .commentCount(safeInt(card.getCommentCount()))
                .tags(tags)
                .qualities(qualities)
                .liked(liked)
                .disliked(disliked)
                .favorited(favorited)
                .followingAuthor(followingAuthor)
                .build();
    }

    public List<Tag> getVideoTags(Long videoId) {
        return tagMapper.selectTagsByVideoId(videoId);
    }

    public PageResult<VideoCardVO> listMyVideos(Long userId, String reviewStatus, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 50);
        IPage<VideoCardVO> result = videoMapper.selectUserVideos(
                new Page<>(safePage, safeSize), userId, reviewStatus, false
        );
        return PageResult.of(result.getCurrent(), result.getSize(), result.getTotal(), result.getRecords());
    }

    @Transactional(rollbackFor = Exception.class)
    public Long publishVideo(Long currentUserId, PublishVideoRequest request) {
        Video video = new Video();
        video.setUserId(currentUserId);
        video.setTitle(request.getTitle().trim());
        video.setDescription(request.getDescription().trim());
        video.setVideoSrcUrl(request.getVideoSrcUrl().trim());
        video.setVideoImgUrl(request.getVideoImgUrl().trim());
        video.setDuration(parseDuration(request.getDuration()));
        video.setUploadDate(LocalDateTime.now());
        video.setViewCount(0);
        video.setLikeCount(0);
        video.setFavoriteCount(0);
        video.setDislikeCount(0);
        video.setBarrageCount(0);
        video.setCommentCount(0);
        video.setReviewStatus("Approved");
        video.setEnable(1);

        videoMapper.insert(video);
        Long videoId = video.getVideoId();

        VideoCategory vc = new VideoCategory();
        vc.setVideoId(videoId);
        vc.setCategoryId(request.getCategoryId());
        vc.setEnable(1);
        videoCategoryMapper.insert(vc);

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            request.getTagIds().stream().distinct().forEach(tagId -> {
                VideoTag vt = new VideoTag();
                vt.setVideoId(videoId);
                vt.setTagId(tagId);
                vt.setEnable(1);
                videoTagMapper.insert(vt);
            });
        }

        userMapper.adjustVideoCount(currentUserId, 1);

        // 仅在启用本地转码时触发异步转码
        if ("local".equalsIgnoreCase(storageProperties.getType()) && request.getVideoSrcUrl().contains("/videos/")) {
            String fileName = request.getVideoSrcUrl().substring(request.getVideoSrcUrl().lastIndexOf('/') + 1);
            String localPath = Paths.get(storageProperties.getLocalDir(), "videos", fileName)
                    .toAbsolutePath().normalize().toString();
            videoTranscodeService.transcodeIfEnabled(videoId, localPath);
        }

        videoSearchService.syncVideo(videoId);
        return videoId;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateVideo(Long currentUserId, Long videoId, UpdateVideoRequest request) {
        Video video = videoMapper.selectById(videoId);
        if (video == null || !Integer.valueOf(1).equals(video.getEnable())) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "视频不存在");
        }
        if (!Objects.equals(video.getUserId(), currentUserId)) {
            throw new ServiceException(ServiceCode.FORBIDDEN, "无权修改他人的稿件");
        }
        video.setTitle(request.getTitle().trim());
        if (request.getDescription() != null) {
            video.setDescription(request.getDescription().trim());
        }
        if (StringUtils.hasText(request.getVideoImgUrl())) {
            video.setVideoImgUrl(request.getVideoImgUrl().trim());
        }
        videoMapper.updateById(video);

        if (request.getCategoryId() != null) {
            videoCategoryMapper.delete(new LambdaQueryWrapper<VideoCategory>().eq(VideoCategory::getVideoId, videoId));
            VideoCategory vc = new VideoCategory();
            vc.setVideoId(videoId);
            vc.setCategoryId(request.getCategoryId());
            vc.setEnable(1);
            videoCategoryMapper.insert(vc);
        }

        if (request.getTagIds() != null) {
            videoTagMapper.delete(new LambdaQueryWrapper<VideoTag>().eq(VideoTag::getVideoId, videoId));
            request.getTagIds().stream().distinct().forEach(tagId -> {
                VideoTag vt = new VideoTag();
                vt.setVideoId(videoId);
                vt.setTagId(tagId);
                vt.setEnable(1);
                videoTagMapper.insert(vt);
            });
        }
        videoSearchService.syncVideo(videoId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteVideo(Long currentUserId, Long videoId) {
        Video video = videoMapper.selectById(videoId);
        if (video == null || !Integer.valueOf(1).equals(video.getEnable())) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "视频不存在");
        }
        if (!Objects.equals(video.getUserId(), currentUserId)) {
            throw new ServiceException(ServiceCode.FORBIDDEN, "无权删除他人的稿件");
        }
        video.setEnable(0);
        videoMapper.updateById(video);
        userMapper.adjustVideoCount(currentUserId, -1);
        videoSearchService.removeVideo(videoId);
    }

    private List<VideoDetailVO.QualityOptionVO> buildQualityOptions(String videoSrcUrl) {
        List<VideoDetailVO.QualityOptionVO> list = new ArrayList<>();
        if (!StringUtils.hasText(videoSrcUrl)) {
            return list;
        }
        // 兼容旧版以 "-" 结尾的多清晰度前缀格式（如 .../3-89/3-89-）
        if (videoSrcUrl.endsWith("-")) {
            list.add(new VideoDetailVO.QualityOptionVO("1080P 高清", videoSrcUrl + "1080.mp4"));
            list.add(new VideoDetailVO.QualityOptionVO("720P 标清", videoSrcUrl + "720.mp4"));
            list.add(new VideoDetailVO.QualityOptionVO("480P 流畅", videoSrcUrl + "480.mp4"));
        } else {
            list.add(new VideoDetailVO.QualityOptionVO("原画 1080P", videoSrcUrl));
        }
        return list;
    }

    private LocalTime parseDuration(String duration) {
        if (!StringUtils.hasText(duration)) {
            return LocalTime.of(0, 0, 0);
        }
        try {
            return LocalTime.parse(duration.trim());
        } catch (Exception e) {
            return LocalTime.of(0, 0, 0);
        }
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }
}
