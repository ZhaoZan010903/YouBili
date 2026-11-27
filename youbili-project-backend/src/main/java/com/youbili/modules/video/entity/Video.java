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
@TableName("videos")
public class Video {

    @TableId(value = "video_id", type = IdType.AUTO)
    private Long videoId;

    @TableField("user_id")
    private Long userId;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("upload_date")
    private LocalDateTime uploadDate;

    @TableField("duration")
    private LocalTime duration;

    @TableField("video_src_url")
    private String videoSrcUrl;

    @TableField("video_img_url")
    private String videoImgUrl;

    @TableField("view_count")
    private Integer viewCount;

    @TableField("like_count")
    private Integer likeCount;

    @TableField("favorite_count")
    private Integer favoriteCount;

    @TableField("dislike_count")
    private Integer dislikeCount;

    @TableField("barrage_count")
    private Integer barrageCount;

    @TableField("comment_count")
    private Integer commentCount;

    @TableField("review_status")
    private String reviewStatus;

    @TableField("enable")
    private Integer enable;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
