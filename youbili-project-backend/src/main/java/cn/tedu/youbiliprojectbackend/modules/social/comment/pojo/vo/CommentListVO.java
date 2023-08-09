package cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentListVO {
    private Long replyID;//回复表
    private String nickname;//用户表
    private String imgUrl;
    private String pNickname;//用户表
    private String replyText;//回复表
    private Integer likeCount;//回复表
    private Integer disLikeCount;//回复表
    private Integer enable;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
}
