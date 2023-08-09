package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.schedule;


import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.service.ThermalValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HeatSchedule {

    @Autowired
    ThermalValueService thermalValueService;


    @Scheduled(cron = "0 0 9 * * *")
    public void HeatSave() {
        thermalValueService.Runner();
    }
}
