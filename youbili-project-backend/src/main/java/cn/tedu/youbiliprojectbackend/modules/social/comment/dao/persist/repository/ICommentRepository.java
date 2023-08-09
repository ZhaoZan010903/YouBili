package cn.tedu.youbiliprojectbackend.modules.social.comment.dao.persist.repository;

import cn.tedu.youbiliprojectbackend.common.utils.pagedate.PageData;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.entity.Comment;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.entity.Commentreplie;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.vo.CommentFirstVO;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.vo.CommentListVO;

import java.util.List;

public interface ICommentRepository {
    PageData<CommentFirstVO> selectByVideoID(Long videoID, Integer pageNum, Integer pageSize);

    List<CommentListVO> selectByCommentID(Long commentID);

    void insertComment(Comment comment);

    void insertCommentreplie(Commentreplie commentreplie);

}
