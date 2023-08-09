package cn.tedu.youbiliprojectbackend.common.data.schedule;

import cn.tedu.youbiliprojectbackend.common.data.dao.persist.cache.DataCacheUserCF;
import cn.tedu.youbiliprojectbackend.common.data.dao.persist.repository.DataRepository;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.service.ThermalValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时跟新类
 */
@Slf4j
@Component
public class RunnerData {
    @Autowired
    DataRepository dataRepository;
    @Autowired
    DataCacheUserCF cacheUserCF;
    @Autowired
    ThermalValueService thermalValueService;

    /**
     * 每天9点会将 点赞收藏历史表的前一天数据写入到mahout表之中
     * 在将数据写入到Redis之中
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void RunnerDataSchedule() {
        log.debug("开始重写数据--------------");
        dataRepository.MahoutData();
        cacheUserCF.selectData();
        thermalValueService.Runner();
    }
}
