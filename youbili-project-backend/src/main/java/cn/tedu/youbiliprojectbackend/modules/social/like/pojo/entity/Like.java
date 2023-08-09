package cn.tedu.youbiliprojectbackend.modules.social.like.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Like implements Serializable {

    /**
     * 点赞ID
     */
    private Long likeID;
    /**
     * 用户ID
     */
    private Long userID;
    /**
     * 视频ID
     */
    private Long videoID;
    /**
     * 点赞时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime likeDate;
    /**
     * 1表示点赞 0表示点踩
     */
    private Integer typeEnable;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtCreate;
    /**
     * 点成点踩时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtModified;
}
