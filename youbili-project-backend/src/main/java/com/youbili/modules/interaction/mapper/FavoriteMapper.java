package com.youbili.modules.interaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youbili.modules.interaction.entity.Favorite;
import com.youbili.modules.interaction.vo.FavoriteItemVO;
import com.youbili.modules.video.vo.VideoCardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

    List<FavoriteItemVO> selectFavoriteList(@Param("userId") Long userId, @Param("keyword") String keyword);

    List<VideoCardVO> selectRecentFavoriteCards(@Param("userId") Long userId, @Param("limit") int limit);
}
