package cn.tedu.youbiliprojectbackend.modules.social.like.controller;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.social.like.pojo.vo.LikeTypeVO;
import cn.tedu.youbiliprojectbackend.modules.social.like.service.ILikeService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 点赞点踩
 *
 * @author 万佳兴
 */

@Slf4j
@RestController
@RequestMapping("/video/handle")
@Validated
public class LikeController {

    @Autowired
    private ILikeService likeService;

    public LikeController() {
        log.info("开始处理点赞点踩的控制器");
    }

    /**
     * 点赞
     *
     * @param principal
     * @return
     */
    @PostMapping("/like")
    public RestBean<?> handleLike(@AuthenticationPrincipal CurrentPrincipal principal, @Validated @NotNull(message = "数据错误!") Long videoID) {
        likeService.addLike(principal.getUserID(), videoID);
        return RestBean.success();
    }

    /**
     * 取消点赞
     *
     * @param principal
     * @return
     */
    @PostMapping("/removelike")
    public RestBean<?> removeLike(@AuthenticationPrincipal CurrentPrincipal principal, @NotNull(message = "数据错误!") Long videoID) {
        likeService.removeLike(principal.getUserID(), videoID);
        return RestBean.success();
    }

    @PostMapping("/dislike")
    public RestBean<?> addDislike(@AuthenticationPrincipal CurrentPrincipal principal, @NotNull(message = "数据错误!") Long videoID) {
        likeService.addDislike(principal.getUserID(), videoID);
        return RestBean.success();
    }

    @PostMapping("/removedislike")
    public RestBean<?> removeDislike(@AuthenticationPrincipal CurrentPrincipal principal, @NotNull(message = "数据错误!") Long videoID) {
        likeService.removeDislike(principal.getUserID(), videoID);
        return RestBean.success();
    }

    @GetMapping("/typeenable")
    public LikeTypeVO getTypeEnable(@AuthenticationPrincipal CurrentPrincipal principal, @NotNull(message = "数据错误!") Long videoID) {
        return likeService.getTypeEnable(principal.getUserID(), videoID);
    }

}
