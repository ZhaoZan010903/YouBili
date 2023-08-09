package cn.tedu.youbiliprojectbackend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class YoubiliProjectBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoubiliProjectBackendApplication.class, args);
        log.debug("------项目启动成功!!!-----");
    }

}
