package com.youbili.modules.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youbili.modules.video.entity.Video;
import com.youbili.modules.video.vo.VideoCardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {

    VideoCardVO selectVideoCardById(@Param("videoId") Long videoId);

    List<VideoCardVO> selectApprovedCards(@Param("limit") int limit);

    List<VideoCardVO> selectCardsByIds(@Param("videoIds") List<Long> videoIds);

    IPage<VideoCardVO> selectCategoryVideos(Page<VideoCardVO> page,
                                            @Param("categoryId") Long categoryId,
                                            @Param("sort") String sort);

    IPage<VideoCardVO> selectUserVideos(Page<VideoCardVO> page,
                                        @Param("userId") Long userId,
                                        @Param("reviewStatus") String reviewStatus,
                                        @Param("onlyApproved") boolean onlyApproved);

    IPage<VideoCardVO> searchVideosByKeyword(Page<VideoCardVO> page,
                                             @Param("keyword") String keyword,
                                             @Param("sort") String sort);

    List<String> suggestTitles(@Param("keyword") String keyword, @Param("limit") int limit);

    IPage<VideoCardVO> selectDynamicFeed(Page<VideoCardVO> page, @Param("userId") Long userId);

    List<VideoCardVO> selectRecentApprovedForHeat(@Param("days") int days, @Param("limit") int limit);

    @Update("UPDATE video SET viewCount = viewCount + 1 WHERE videoID = #{videoId}")
    int incrementViewCount(@Param("videoId") Long videoId);

    @Update("UPDATE video SET likeCount = GREATEST(0, likeCount + #{delta}) WHERE videoID = #{videoId}")
    int adjustLikeCount(@Param("videoId") Long videoId, @Param("delta") int delta);

    @Update("UPDATE video SET dislikeCount = GREATEST(0, dislikeCount + #{delta}) WHERE videoID = #{videoId}")
    int adjustDislikeCount(@Param("videoId") Long videoId, @Param("delta") int delta);

    @Update("UPDATE video SET favoriteCount = GREATEST(0, favoriteCount + #{delta}) WHERE videoID = #{videoId}")
    int adjustFavoriteCount(@Param("videoId") Long videoId, @Param("delta") int delta);

    @Update("UPDATE video SET barrageCount = GREATEST(0, barrageCount + #{delta}) WHERE videoID = #{videoId}")
    int adjustBarrageCount(@Param("videoId") Long videoId, @Param("delta") int delta);

    @Update("UPDATE video SET commentCount = GREATEST(0, commentCount + #{delta}) WHERE videoID = #{videoId}")
    int adjustCommentCount(@Param("videoId") Long videoId, @Param("delta") int delta);
}
