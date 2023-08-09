package cn.tedu.youbiliprojectbackend.modules.user.favorite.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("favorite")
public class Favorite implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long favoriteID;

    private Long userID;

    private Long videoID;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date favoriteDate;

    private Integer enable;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}
