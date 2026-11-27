package com.youbili.modules.category.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("categorie")
public class Category {

    @TableId(value = "categoryID", type = IdType.AUTO)
    private Long categoryId;

    @TableField("categoryName")
    private String categoryName;

    @TableField("description")
    private String description;

    @TableField("enable")
    private Integer enable;

    @TableField(value = "gmtCreate", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmtModified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
