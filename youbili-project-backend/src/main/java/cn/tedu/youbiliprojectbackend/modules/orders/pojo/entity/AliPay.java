package cn.tedu.youbiliprojectbackend.modules.orders.pojo.entity;

import lombok.Data;

@Data
public class AliPay {
    private String out_trade_no;
    private Double total_amount;
    private String subject;
    private String alipay_trace_no;
}