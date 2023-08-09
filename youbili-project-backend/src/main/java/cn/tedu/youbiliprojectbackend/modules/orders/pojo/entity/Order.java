package cn.tedu.youbiliprojectbackend.modules.orders.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order implements Serializable {
    /**
     * 订单ID
     */
    @TableField(value = "orderID")
    @TableId(type = IdType.AUTO)
    private Long orderID;
    /**
     * 商品名称
     */
    private String tradeName;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 支付宝订单号
     */
    private String zfbNumber;
    /**
     * 价格
     */
    private Double price;
    /**
     * 支付方式
     */
    private String payment;
    /**
     * 支付时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime payTime;
    /**
     * 订单状态
     */
    private String enable;
    /**
     * 购买人ID
     */
    private Long userID;
}
