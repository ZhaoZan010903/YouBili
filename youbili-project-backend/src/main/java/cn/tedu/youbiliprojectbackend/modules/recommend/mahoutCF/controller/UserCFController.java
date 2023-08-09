package cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.controller;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.service.UserCFService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

/**
 * 推荐方法
 *
 * @author 赵錾
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class UserCFController {
    @Autowired
    UserCFService userCFService;

    /**
     * 首页推荐
     *
     * @param principal 当事人ID
     * @return 视频列表
     */
    @GetMapping("/usercf")
    public RestBean<List<VideoUserCache>> selectList(@AuthenticationPrincipal CurrentPrincipal principal) {
        List<VideoUserCache> videoUserCaches = null;
        if (principal == null) {
            log.debug("开始处理");
            videoUserCaches = userCFService.UserCF(0L, 12);
        } else {
            videoUserCaches = userCFService.UserCF(principal.getUserID(), 12);
        }
        return RestBean.success(videoUserCaches);
    }

    /**
     * 推荐轮播图
     *
     * @param principal
     * @return
     */
    @GetMapping("/videoCF")
    public RestBean<List<VideoUserCache>> Carousel(@AuthenticationPrincipal CurrentPrincipal principal) {
        List<VideoUserCache> videoUserCaches = null;
        Random random = new Random();
        long l = random.nextLong(5) + 1;
        if (principal == null) {
            log.debug("开始处理");
            videoUserCaches = userCFService.UserCF(0L, 7);
        } else {
            videoUserCaches = userCFService.UserCF(principal.getUserID(), 7);
        }
        return RestBean.success(videoUserCaches);
    }


}
