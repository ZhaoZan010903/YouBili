package com.youbili.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youbili.modules.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("UPDATE `user` SET fans = GREATEST(0, fans + #{delta}) WHERE userID = #{userId}")
    int adjustFans(@Param("userId") Long userId, @Param("delta") int delta);

    @Update("UPDATE `user` SET follows = GREATEST(0, follows + #{delta}) WHERE userID = #{userId}")
    int adjustFollows(@Param("userId") Long userId, @Param("delta") int delta);

    @Update("UPDATE `user` SET videoCount = GREATEST(0, videoCount + #{delta}) WHERE userID = #{userId}")
    int adjustVideoCount(@Param("userId") Long userId, @Param("delta") int delta);

    @Update("UPDATE `user` SET totalLikes = GREATEST(0, totalLikes + #{delta}) WHERE userID = #{userId}")
    int adjustTotalLikes(@Param("userId") Long userId, @Param("delta") int delta);

    @Update("UPDATE `user` SET membershipStatus = #{status} WHERE userID = #{userId}")
    int updateMembershipStatus(@Param("userId") Long userId, @Param("status") String status);

    @Update("UPDATE `user` SET lastLoginDate = NOW() WHERE userID = #{userId}")
    int updateLastLoginDate(@Param("userId") Long userId);
}
