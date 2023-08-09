package cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Long commentID;
    private Long userID;
    private Long videoID;
    private String commentText;
    private String commentDate;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer enable;
    /**
     * 数据创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 数据最后修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
