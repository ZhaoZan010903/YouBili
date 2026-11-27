package com.youbili.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youbili.modules.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("UPDATE users SET fans = GREATEST(0, fans + #{delta}) WHERE user_id = #{userId}")
    int adjustFans(@Param("userId") Long userId, @Param("delta") int delta);

    @Update("UPDATE users SET follows = GREATEST(0, follows + #{delta}) WHERE user_id = #{userId}")
    int adjustFollows(@Param("userId") Long userId, @Param("delta") int delta);

    @Update("UPDATE users SET video_count = GREATEST(0, video_count + #{delta}) WHERE user_id = #{userId}")
    int adjustVideoCount(@Param("userId") Long userId, @Param("delta") int delta);

    @Update("UPDATE users SET total_likes = GREATEST(0, total_likes + #{delta}) WHERE user_id = #{userId}")
    int adjustTotalLikes(@Param("userId") Long userId, @Param("delta") int delta);

    @Update("UPDATE users SET membership_status = #{status} WHERE user_id = #{userId}")
    int updateMembershipStatus(@Param("userId") Long userId, @Param("status") String status);

    @Update("UPDATE users SET last_login_date = NOW() WHERE user_id = #{userId}")
    int updateLastLoginDate(@Param("userId") Long userId);
}
