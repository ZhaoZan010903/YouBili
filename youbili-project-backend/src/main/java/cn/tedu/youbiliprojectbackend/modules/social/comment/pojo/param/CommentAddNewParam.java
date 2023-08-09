package cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentAddNewParam implements Serializable {
    private Long videoID;
    private String commentText;
}
