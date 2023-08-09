package cn.tedu.youbiliprojectbackend.modules.social.comment.dao.persist.mapper;

import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.entity.Comment;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.vo.CommentFirstVO;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.vo.CommentListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


public interface CommentMapper extends BaseMapper<Comment> {
    List<CommentFirstVO> selectFirstCommentByVideoID(Long videoID);

    List<CommentListVO> selectCommentListByCommentID(Long commentID);
}
