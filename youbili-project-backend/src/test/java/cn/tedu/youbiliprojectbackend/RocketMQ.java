package cn.tedu.youbiliprojectbackend;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RocketMQ {
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Test
    void produc() {
        rocketMQTemplate.destroy();
    }
}
