package com.youbili.modules.social.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youbili.common.exception.ServiceException;
import com.youbili.common.response.PageResult;
import com.youbili.common.response.ServiceCode;
import com.youbili.modules.social.dto.CreateCommentRequest;
import com.youbili.modules.social.dto.CreateReplyRequest;
import com.youbili.modules.social.entity.Comment;
import com.youbili.modules.social.entity.CommentReply;
import com.youbili.modules.social.mapper.CommentMapper;
import com.youbili.modules.social.mapper.CommentReplyMapper;
import com.youbili.modules.social.vo.CommentReplyVO;
import com.youbili.modules.social.vo.CommentVO;
import com.youbili.modules.video.entity.Video;
import com.youbili.modules.video.mapper.VideoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final CommentReplyMapper commentReplyMapper;
    private final VideoMapper videoMapper;

    public PageResult<CommentVO> listComments(Long videoId, int page, int size, String sort) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 50);
        IPage<CommentVO> topPage = commentMapper.selectTopComments(new Page<>(safePage, safeSize), videoId, sort);
        List<CommentVO> records = topPage.getRecords();

        if (!records.isEmpty()) {
            List<Long> commentIds = records.stream().map(CommentVO::getCommentId).toList();
            List<CommentReplyVO> allReplies = commentMapper.selectRepliesByCommentIds(commentIds);
            Map<Long, List<CommentReplyVO>> grouped = allReplies.stream()
                    .collect(Collectors.groupingBy(CommentReplyVO::getCommentId));
            for (CommentVO comment : records) {
                List<CommentReplyVO> replies = grouped.getOrDefault(comment.getCommentId(), Collections.emptyList());
                comment.setReplies(replies);
                comment.setReplyCount(replies.size());
            }
        }

        return PageResult.of(topPage.getCurrent(), topPage.getSize(), topPage.getTotal(), records);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addComment(Long videoId, Long userId, CreateCommentRequest request) {
        Video video = videoMapper.selectById(videoId);
        if (video == null || !Integer.valueOf(1).equals(video.getEnable())) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "视频不存在");
        }
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setVideoId(videoId);
        comment.setCommentText(request.getCommentText().trim());
        comment.setCommentDate(LocalDateTime.now());
        comment.setLikeCount(0);
        comment.setDislikeCount(0);
        comment.setEnable(1);
        commentMapper.insert(comment);
        videoMapper.adjustCommentCount(videoId, 1);
        return comment.getCommentId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addReply(Long commentId, Long userId, CreateReplyRequest request) {
        Comment parent = commentMapper.selectById(commentId);
        if (parent == null || !Integer.valueOf(1).equals(parent.getEnable())) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "主评论不存在");
        }
        CommentReply reply = new CommentReply();
        reply.setCommentId(commentId);
        reply.setParentReplyId(request.getParentReplyId() != null && request.getParentReplyId() > 0
                ? request.getParentReplyId() : null);
        reply.setUserId(userId);
        reply.setReplyText(request.getReplyText().trim());
        reply.setReplyDate(LocalDateTime.now());
        reply.setLikeCount(0);
        reply.setDislikeCount(0);
        reply.setEnable(1);
        commentReplyMapper.insert(reply);
        videoMapper.adjustCommentCount(parent.getVideoId(), 1);
        return reply.getReplyId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId, Long userId) {
        Comment existing = commentMapper.selectById(commentId);
        if (existing == null || !Integer.valueOf(1).equals(existing.getEnable())) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "评论不存在");
        }
        if (!Objects.equals(existing.getUserId(), userId)) {
            throw new ServiceException(ServiceCode.FORBIDDEN, "无权删除他人评论");
        }
        existing.setEnable(0);
        commentMapper.updateById(existing);
        videoMapper.adjustCommentCount(existing.getVideoId(), -1);
    }
}
