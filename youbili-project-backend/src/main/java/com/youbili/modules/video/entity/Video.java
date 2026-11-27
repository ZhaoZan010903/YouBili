package com.youbili.modules.video.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("video")
public class Video {

    @TableId(value = "videoID", type = IdType.AUTO)
    private Long videoId;

    @TableField("userID")
    private Long userId;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("uploadDate")
    private LocalDateTime uploadDate;

    @TableField("duration")
    private LocalTime duration;

    @TableField("videoSrcUrl")
    private String videoSrcUrl;

    @TableField("videoImgUrl")
    private String videoImgUrl;

    @TableField("viewCount")
    private Integer viewCount;

    @TableField("likeCount")
    private Integer likeCount;

    @TableField("favoriteCount")
    private Integer favoriteCount;

    @TableField("dislikeCount")
    private Integer dislikeCount;

    @TableField("barrageCount")
    private Integer barrageCount;

    @TableField("commentCount")
    private Integer commentCount;

    @TableField("reviewStatus")
    private String reviewStatus;

    @TableField("enable")
    private Integer enable;

    @TableField(value = "gmtCreate", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmtModified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
