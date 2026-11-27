package com.youbili.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youbili.modules.user.entity.Follow;
import com.youbili.modules.user.vo.FollowUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

    List<FollowUserVO> selectFollowList(@Param("userId") Long userId);

    List<FollowUserVO> selectFanList(@Param("userId") Long userId);

    List<Long> selectFollowedUserIds(@Param("userId") Long userId);
}
