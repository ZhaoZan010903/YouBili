package cn.tedu.youbiliprojectbackend.modules.user.follow.controller;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo.FanListVO;
import cn.tedu.youbiliprojectbackend.modules.user.follow.service.IFanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 肖爽
 */
@Slf4j
@RestController
@RequestMapping("/user/fans")
public class FanController {
    @Autowired
    IFanService fanService;

    FanController() {
        log.debug("创建控制器类: FanController ");
    }

    /**
     * 加载当前登录用户的关注列表
     *
     * @param principal 当事人
     * @return 返回当前用户关注的人
     */
    @GetMapping("/fanlist")
    public RestBean<List<FanListVO>> followList(@AuthenticationPrincipal CurrentPrincipal principal) {
        log.debug("开始处理请求 fanlist");
        List<FanListVO> list = fanService.list(principal.getUserID());

        return RestBean.success(list);
    }

    /**
     * 删除粉丝
     *
     * @param principal 当事人
     * @param followID  关注表的ID
     * @return
     */
    @GetMapping("/{followID}/deletefan")
    public RestBean<String> deleteFan(@AuthenticationPrincipal CurrentPrincipal principal, @PathVariable Long followID) {
        fanService.delete(principal.getUserID(), followID);
        return RestBean.success("删除粉丝成功!");
    }
}
