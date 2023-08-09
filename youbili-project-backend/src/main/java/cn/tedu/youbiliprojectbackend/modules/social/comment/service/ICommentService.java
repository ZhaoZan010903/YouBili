package cn.tedu.youbiliprojectbackend.modules.social.comment.service;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.utils.pagedate.PageData;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.param.CommentAddNewParam;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.param.CommentReplyAddNewParam;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.vo.CommentFirstVO;

public interface ICommentService {
    PageData<CommentFirstVO> getCommentTreeByVideoID(Long videoID, Integer pageNum);

    void insertComment(CommentAddNewParam commentAddNewParam, CurrentPrincipal principal);

    void insertCommentreplie(CommentReplyAddNewParam commentReplyAddNewParam, CurrentPrincipal principal);
}
