package com.youbili.modules.category.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youbili.modules.category.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    @Select("""
            SELECT t.tag_id, t.tag_name, t.description
            FROM tags t
            INNER JOIN video_tags vt ON t.tag_id = vt.tag_id
            WHERE vt.video_id = #{videoId} AND vt.enable = 1 AND t.enable = 1
            """)
    List<Tag> selectTagsByVideoId(@Param("videoId") Long videoId);
}
