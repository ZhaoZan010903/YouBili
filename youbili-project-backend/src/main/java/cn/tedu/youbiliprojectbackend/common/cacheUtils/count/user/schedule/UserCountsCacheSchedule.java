package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.schedule;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.service.IUserCountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCountsCacheSchedule implements ApplicationRunner {
    @Autowired
    private IUserCountsService service;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("项目启动,正在处理缓存用户计数字段业务");
        service.saveCacheUserCounts();
    }
}
