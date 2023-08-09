package cn.tedu.youbiliprojectbackend.modules.search.controller;

import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.search.pojo.vo.VideoSearchVO;
import cn.tedu.youbiliprojectbackend.modules.search.service.IVideoSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/auth/search")
public class VideoSearchController {
    @Autowired
    private IVideoSearchService videoSearchService;

    public VideoSearchController() {
        log.info("创建类别控制器:VideoSearchController");
    }

    @GetMapping("/index")
    public RestBean<List<VideoSearchVO>> searchBtTitle(String title) {
        log.debug("开始处理【查询根据用户输入标题查询】的请求");
        List<VideoSearchVO> videoSearchVOList = videoSearchService.searchByTitle(title);
        System.out.println(videoSearchVOList);
        return RestBean.success(videoSearchVOList);
    }
}
