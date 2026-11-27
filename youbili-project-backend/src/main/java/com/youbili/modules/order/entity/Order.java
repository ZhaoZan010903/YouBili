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

    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    @TableField("trade_name")
    private String tradeName;

    @TableField("order_number")
    private String orderNumber;

    @TableField("zfb_number")
    private String zfbNumber;

    @TableField("price")
    private BigDecimal price;

    @TableField("payment")
    private String payment;

    @TableField("user_id")
    private Long userId;

    @TableField("pay_time")
    private LocalDateTime payTime;

    @TableField("status")
    private String status;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
