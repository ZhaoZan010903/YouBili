package cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentFirstVO implements Serializable {
    private Long commentID;//comment
    private String nickname;//user
    private String imgUrl;//用户头像
    private String commentText;//comment
    private Integer likeCount;//comment
    private Integer dislikeCount;
    private Boolean isShow;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    //   总回复数
    private Integer commentListLength;
    // 回复列表
    private List<CommentListVO> commentList;
}
