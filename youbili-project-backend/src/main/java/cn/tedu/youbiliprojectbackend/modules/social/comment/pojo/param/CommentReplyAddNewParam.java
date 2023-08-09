package cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentReplyAddNewParam implements Serializable {
    private Long commentID;
    private Long parentReplyID;
    private String replyText;
}
