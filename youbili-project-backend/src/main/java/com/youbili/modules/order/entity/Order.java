package com.youbili.modules.order.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {

    @TableId(value = "orderID", type = IdType.AUTO)
    private Long orderId;

    @TableField("tradeName")
    private String tradeName;

    @TableField("orderNumber")
    private String orderNumber;

    @TableField("zfbNumber")
    private String zfbNumber;

    @TableField("price")
    private BigDecimal price;

    @TableField("payment")
    private String payment;

    @TableField("userID")
    private Long userId;

    @TableField("payTime")
    private LocalDateTime payTime;

    @TableField("enable")
    private String status;

    @TableField(value = "gmtCreate", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmtModified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
