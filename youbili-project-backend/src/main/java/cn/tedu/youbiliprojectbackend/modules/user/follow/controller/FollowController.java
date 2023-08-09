package cn.tedu.youbiliprojectbackend.modules.user.follow.controller;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo.FollowListVO;
import cn.tedu.youbiliprojectbackend.modules.user.follow.service.IFollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 关注控制器类
 *
 * @author 肖爽
 */
@Slf4j
@RestController
@RequestMapping("/user/follow")
public class FollowController {

    @Autowired
    IFollowService followService;

    FollowController() {
        log.debug("创建控制器类: FollowController ");
    }

    /**
     * 加载当前登录用户的关注列表
     *
     * @param principal 当事人ID
     * @return 返回当前用户关注的人
     */
    @GetMapping("/followlist")
    public RestBean<List<FollowListVO>> followList(@AuthenticationPrincipal CurrentPrincipal principal) {
        log.debug("开始处理请求 followlist");
        List<FollowListVO> list = followService.list(principal.getUserID());
        System.out.println(list);
        return RestBean.success(list);
    }

    /**
     * 添加关注
     *
     * @param principal 当事人
     * @param userID    被关注者的ID
     * @return
     */
    @GetMapping("/addfollow")
    public RestBean<String> addFollow(@AuthenticationPrincipal CurrentPrincipal principal, Long userID) {
        log.debug("userID: {}", userID);
//        Long followedID = followService.selectByFollowIdSer(userID);
//        log.debug("followedID: {}",followedID);
        followService.addFollow(principal.getUserID(), userID);
        return RestBean.success("添加关注成功!");
    }

    /**
     * 取消关注
     *
     * @param principal 当事人
     * @param followID  关注表的ID
     * @return
     */
    @GetMapping("/deletefollow")
    public RestBean<String> deleteFollow(@AuthenticationPrincipal CurrentPrincipal principal, Long followID) {

        log.debug("followID: {}", followID);
        followService.delete(principal.getUserID(), followID);
//        followService.deleteAll(1L,3L);
        return RestBean.success("取消关注成功!");
    }

}
