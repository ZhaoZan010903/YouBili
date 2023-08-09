package cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 肖爽
 */
@Data
@TableName("follow")
public class Follow implements Serializable {
    private Long followID;
    @TableField(value = "followerID")
    private Long followerID;
    @TableField(value = "followedID")
    private Long followedID;
    private Date followDate;
    private Integer enable;
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}
