package cn.tedu.youbiliprojectbackend.common.RocketMQ;

import cn.tedu.youbiliprojectbackend.modules.video.uplaod.controller.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author 张岩
 */
@Component
@Slf4j
public class RocketMQRunner implements ApplicationRunner {
    @Autowired
    private Consumer consumer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("------------开始!!!消息队列---------------");
        consumer.consumer();
    }
}
