package cn.tedu.youbiliprojectbackend.modules.social.comment.service.impl;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.utils.pagedate.PageData;
import cn.tedu.youbiliprojectbackend.modules.social.comment.dao.persist.repository.ICommentRepository;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.entity.Comment;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.entity.Commentreplie;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.param.CommentAddNewParam;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.param.CommentReplyAddNewParam;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.vo.CommentFirstVO;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.vo.CommentListVO;
import cn.tedu.youbiliprojectbackend.modules.social.comment.service.ICommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private ICommentRepository repository;

    @Override
    public PageData<CommentFirstVO> getCommentTreeByVideoID(Long videoID, Integer pageNum) {
        PageData<CommentFirstVO> commentFirstVOS = repository.selectByVideoID(videoID, pageNum, 500);
        List<CommentFirstVO> list = commentFirstVOS.getList();
        for (CommentFirstVO commentFirstVO : list) {
            List<CommentListVO> commentListVOS = repository.selectByCommentID(commentFirstVO.getCommentID());
            commentFirstVO.setCommentList(commentListVOS);
            commentFirstVO.setCommentListLength(commentListVOS.size());
            commentFirstVO.setIsShow(commentListVOS.size() > 3);
        }

        return commentFirstVOS;
    }

    @Override
    public void insertComment(CommentAddNewParam commentAddNewParam, CurrentPrincipal principal) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentAddNewParam, comment);
        comment.setUserID(principal.getUserID());
        repository.insertComment(comment);
    }

    @Override
    public void insertCommentreplie(CommentReplyAddNewParam commentReplyAddNewParam, CurrentPrincipal principal) {
        Commentreplie commentreplie = new Commentreplie();
        BeanUtils.copyProperties(commentReplyAddNewParam, commentreplie);
        commentreplie.setUserID(principal.getUserID());
        repository.insertCommentreplie(commentreplie);
    }
}
