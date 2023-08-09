package cn.tedu.youbiliprojectbackend.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 阿里支付宝沙箱配置
 *
 * @author 万佳兴
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {
    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String returnUrl;
    private String notifyUrl;
}
