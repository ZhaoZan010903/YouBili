package cn.tedu.youbiliprojectbackend.modules.user.personal.controller;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.user.personal.pojo.vo.PersonalCenterInfoVO;
import cn.tedu.youbiliprojectbackend.modules.user.personal.service.IPersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/personal/")
public class personalController {
    @Autowired
    IPersonalService service;

    @GetMapping("{userId}/get-center")
    public RestBean<Object> getUserInfo(@PathVariable Long userId, @AuthenticationPrincipal CurrentPrincipal principal) {
        if (userId == 0) {
            userId = principal.getUserID();
        }
        PersonalCenterInfoVO centerInfo = service.getCenterInfoById(userId);
        return RestBean.success(centerInfo);
    }
}
