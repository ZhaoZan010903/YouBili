package cn.tedu.youbiliprojectbackend.mahout;

import cn.tedu.youbiliprojectbackend.common.data.mapper.DataMapper;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.controller.HeatController;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache.SaveAndDelete;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache.heatCache;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.persist.mapper.ThermaMapper;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.HeatVideo;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.score.ThermalValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Heat {
    @Autowired
    heatCache heatCache;
    @Autowired
    ThermalValue thermalValue;
    @Autowired
    DataMapper dataMapper;
    @Autowired
    SaveAndDelete saveAndDelete;
    @Autowired
    HeatController heatController;

    @Test
    void heat() {
        List<HeatVideo> heatVideos = thermalValue.heatValue();
        System.out.println(heatVideos);
//        System.out.println(heatCache.listCount());
    }

    @Test
    void setSaveAndDelete() {
        heatController.heat();
    }


}
