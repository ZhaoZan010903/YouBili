package com.youbili.modules.recommend.mapper;

import com.youbili.modules.recommend.entity.UserBehaviorScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecommendMapper {

    List<UserBehaviorScore> selectAggregatedUserBehaviors(@Param("limit") int limit);
}
