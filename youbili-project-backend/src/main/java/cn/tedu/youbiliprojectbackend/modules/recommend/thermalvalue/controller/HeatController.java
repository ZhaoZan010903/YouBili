package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.controller;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.service.ThermalValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 热门查询控制器
 *
 * @author 赵錾
 */
@RestController
@Slf4j
@RequestMapping("/api/auth")
public class HeatController {
    @Autowired
    ThermalValueService thermalValueService;

    HeatController() {
        log.debug("创建HeatController");
    }

    @GetMapping("/heat")
    public RestBean<List<VideoUserCache>> heat() {
        List<VideoUserCache> select = thermalValueService.select();
        return RestBean.success(select);
    }


    @GetMapping("/carousel")
    public RestBean<List<VideoUserCache>> carousel() {
        List<VideoUserCache> select = thermalValueService.select1();
        return RestBean.success(select);
    }
}
