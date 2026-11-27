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

    @Update("UPDATE videos SET view_count = view_count + 1 WHERE video_id = #{videoId}")
    int incrementViewCount(@Param("videoId") Long videoId);

    @Update("UPDATE videos SET like_count = GREATEST(0, like_count + #{delta}) WHERE video_id = #{videoId}")
    int adjustLikeCount(@Param("videoId") Long videoId, @Param("delta") int delta);

    @Update("UPDATE videos SET dislike_count = GREATEST(0, dislike_count + #{delta}) WHERE video_id = #{videoId}")
    int adjustDislikeCount(@Param("videoId") Long videoId, @Param("delta") int delta);

    @Update("UPDATE videos SET favorite_count = GREATEST(0, favorite_count + #{delta}) WHERE video_id = #{videoId}")
    int adjustFavoriteCount(@Param("videoId") Long videoId, @Param("delta") int delta);

    @Update("UPDATE videos SET barrage_count = GREATEST(0, barrage_count + #{delta}) WHERE video_id = #{videoId}")
    int adjustBarrageCount(@Param("videoId") Long videoId, @Param("delta") int delta);

    @Update("UPDATE videos SET comment_count = GREATEST(0, comment_count + #{delta}) WHERE video_id = #{videoId}")
    int adjustCommentCount(@Param("videoId") Long videoId, @Param("delta") int delta);
}
