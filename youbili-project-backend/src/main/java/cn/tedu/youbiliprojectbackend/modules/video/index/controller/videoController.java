package cn.tedu.youbiliprojectbackend.modules.video.index.controller;

import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.video.index.pojo.vo.VideoTagVO;
import cn.tedu.youbiliprojectbackend.modules.video.index.pojo.vo.videoListVO;
import cn.tedu.youbiliprojectbackend.modules.video.index.service.IVideoListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/video")
@Slf4j
public class videoController {

    @Autowired
    IVideoListService videoListService;


    @GetMapping("/videoList")
    public RestBean<videoListVO> videoUpdate(Long videoID) {
        log.debug("Controller: 拿到的videoID:{}----------------------------------", videoID);
        videoListVO videoListVO = videoListService.selectAll(videoID);
        return RestBean.success(videoListVO);
    }

    @GetMapping("/taglist")
    public RestBean<List<VideoTagVO>> tagList(Long videoID) {
        List<VideoTagVO> videoTagVOS = videoListService.selectTag(videoID);
        return RestBean.success(videoTagVOS);
    }


}
