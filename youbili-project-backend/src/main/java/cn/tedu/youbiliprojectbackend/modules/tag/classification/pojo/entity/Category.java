package cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 类别的实体类
 *
 * @author 万佳兴
 */
@Data
@TableName("category")
public class Category implements Serializable {
    /**
     * 分类ID
     */
    @TableId(type = IdType.AUTO)
    private Long categoryID;
    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 分类描述
     */
    private String description;
    /**
     * 状态 1表示启用 0表示未启用
     */
    private Integer enable;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtModified;
}
