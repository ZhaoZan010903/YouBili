package cn.tedu.youbiliprojectbackend.modules.user.favorite.controller;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.user.favorite.pojo.vo.FavoriteVO;
import cn.tedu.youbiliprojectbackend.modules.user.favorite.service.IFavoriteService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ASUS
 */
@RestController
@RequestMapping("/user/favorite")
@Slf4j
@Validated
public class FavoriteController {

    @Autowired
    IFavoriteService favoriteService;


    FavoriteController() {
        log.debug("创建控制器类: FavoriteController");
    }

    /**
     * 删除收藏视频
     *
     * @param currentPrincipal 当事人信息
     * @param favoriteID       收藏视频ID
     * @return 返回响应结果
     */
    @GetMapping("delete")
    public RestBean<String> delete(@AuthenticationPrincipal CurrentPrincipal currentPrincipal,
                                   @NotNull Long favoriteID, @NotNull Long videoID) {
        log.debug("开始处理请求: deleteAll");
        favoriteService.deleteById(favoriteID, currentPrincipal.getUserID(), videoID);
        return RestBean.success("删除成功!");
    }

    /**
     * 查询收藏列表
     *
     * @param currentPrincipal 当事人信息
     * @return 返回收藏列表
     */
    @GetMapping("/all-list")
    public RestBean<List<FavoriteVO>> allList(@AuthenticationPrincipal CurrentPrincipal currentPrincipal) {
        log.debug("开始处理请求 all-list");
        Long userID = currentPrincipal.getUserID();
        List<FavoriteVO> list = favoriteService.list(userID);
        return RestBean.success(list);
    }

    /**
     * 添加收藏
     *
     * @param currentPrincipal 当事人ID
     * @param videoID          视频ID
     * @return 返回
     */
    @PutMapping("/add-favorite")
    public RestBean<String> addFavorite(@AuthenticationPrincipal CurrentPrincipal currentPrincipal, @NotNull Long videoID) {
        log.debug("开始处理请求 add-favorite");
        log.debug("-------videoID:{}", videoID);
        favoriteService.insterFavorite(currentPrincipal.getUserID(), videoID);
        return RestBean.success("收藏成功");
    }

    /**
     * 删除收藏
     * <p>
     * 视频页面
     */
    @PutMapping("/deletefavorite")
    public RestBean<?> deleteFavorite(@AuthenticationPrincipal CurrentPrincipal currentPrincipal, @NotNull Long videoID) {
        log.debug("开始处理 /deletefavorite");
        favoriteService.deleteByUserIDandVideoID(currentPrincipal.getUserID(), videoID);
        return RestBean.success();
    }

}
