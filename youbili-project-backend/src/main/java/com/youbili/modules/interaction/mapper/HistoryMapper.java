package com.youbili.modules.interaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youbili.modules.interaction.entity.WatchHistory;
import com.youbili.modules.interaction.vo.HistoryItemVO;
import com.youbili.modules.video.vo.VideoCardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface HistoryMapper extends BaseMapper<WatchHistory> {

    List<HistoryItemVO> selectHistoryList(@Param("userId") Long userId, @Param("keyword") String keyword);

    List<VideoCardVO> selectRecentHistoryCards(@Param("userId") Long userId, @Param("limit") int limit);

    @Update("UPDATE history SET enable = 0 WHERE userID = #{userId} AND enable = 1")
    int clearAllByUserId(@Param("userId") Long userId);
}
