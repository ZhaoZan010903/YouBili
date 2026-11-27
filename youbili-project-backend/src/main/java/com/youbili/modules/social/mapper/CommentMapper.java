package com.youbili.modules.social.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youbili.modules.social.entity.Comment;
import com.youbili.modules.social.vo.CommentReplyVO;
import com.youbili.modules.social.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<CommentVO> selectTopComments(Page<CommentVO> page,
                                       @Param("videoId") Long videoId,
                                       @Param("sort") String sort);

    List<CommentReplyVO> selectRepliesByCommentIds(@Param("commentIds") List<Long> commentIds);
}
