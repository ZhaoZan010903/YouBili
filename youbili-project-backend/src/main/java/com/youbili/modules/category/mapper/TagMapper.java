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
            SELECT t.tagID AS tagId, t.tagName AS tagName, t.description AS description
            FROM tag t
            INNER JOIN videotag vt ON t.tagID = vt.tagID
            WHERE vt.videoID = #{videoId} AND vt.enable = 1 AND t.enable = 1
            """)
    List<Tag> selectTagsByVideoId(@Param("videoId") Long videoId);
}
