package cn.tedu.youbiliprojectbackend.modules.social.comment.controller;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.param.CommentAddNewParam;
import cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.param.CommentReplyAddNewParam;
import cn.tedu.youbiliprojectbackend.modules.social.comment.service.ICommentService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/auth/comment/")
@Validated
public class CommentController {
    @Autowired
    private ICommentService service;

    @GetMapping("{videoID}/get-comment")
    public RestBean getComment(@PathVariable Long videoID, Integer page) {
        Integer pageNum = page == null ? 1 : page;
        return RestBean.success(service.getCommentTreeByVideoID(videoID, pageNum));
    }

    @PostMapping("add-new")
    public RestBean addNewComment(CommentAddNewParam param, @AuthenticationPrincipal @NotNull(message = "您还为登录") CurrentPrincipal principal) {
        service.insertComment(param, principal);
        log.warn(param.toString());
        return RestBean.success();
    }

    @PostMapping("/reply/add-new")
    public RestBean addNewReply(CommentReplyAddNewParam param, @AuthenticationPrincipal @NotNull(message = "您还为登录") CurrentPrincipal principal) {
        service.insertCommentreplie(param, principal);
        log.warn(param.toString());
        return RestBean.success();
    }
}
