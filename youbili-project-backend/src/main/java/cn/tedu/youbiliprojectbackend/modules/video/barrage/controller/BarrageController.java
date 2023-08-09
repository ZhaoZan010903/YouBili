package cn.tedu.youbiliprojectbackend.modules.video.barrage.controller;

import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.video.barrage.pojo.DTO.BarrageDTO;
import cn.tedu.youbiliprojectbackend.modules.video.barrage.pojo.VO.BarrageVO;
import cn.tedu.youbiliprojectbackend.modules.video.barrage.pojo.entity.Barrage;
import cn.tedu.youbiliprojectbackend.modules.video.barrage.server.impl.BarrageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
//@RequestMapping("/auth/barrages")
public class BarrageController {
    @Autowired
    private BarrageServiceImpl barrageService;

    @GetMapping("/api/video/loadBarrage")
    public RestBean<List<BarrageVO>> getBarrageList(Long videoID) {
        log.debug("开始处理加载弹幕列表请求!");
        log.debug("{}", videoID);
        List<BarrageVO> list = barrageService.findByVideoID(videoID);
        return RestBean.success(list);
    }

    //    @PostMapping("/barrages/sendBarrage")
    @PutMapping("/barrages/sendBarrage")
    public RestBean<Barrage> sendBarrage(BarrageDTO barrageDTO) {
        log.debug("开始处理新增弹幕请求!");
        log.debug("{}", barrageDTO);
        barrageService.sent(barrageDTO);
        return RestBean.success();
    }

}
