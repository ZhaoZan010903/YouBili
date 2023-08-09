package cn.tedu.youbiliprojectbackend.modules.social.comment.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("commentreplie")
public class Commentreplie {
    private Long replyID;
    private Long commentID;
    private Long parentReplyID;
    private Long userID;
    private String replyText;
    private String replyDate;
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
