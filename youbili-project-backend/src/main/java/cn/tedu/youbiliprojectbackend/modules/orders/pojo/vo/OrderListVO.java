package cn.tedu.youbiliprojectbackend.modules.orders.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrderListVO implements Serializable {

    /**
     * 订单ID
     */
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;
    /**
     * 购买人ID
     */
    private Long userID;
    /**
     * 支付状态
     */
    private String enable;
    /**
     * 状态
     */
    private Boolean status;
}
