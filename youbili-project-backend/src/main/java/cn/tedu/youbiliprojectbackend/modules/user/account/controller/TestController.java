package cn.tedu.youbiliprojectbackend.modules.user.account.controller;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class TestController {

    /**
     * 测试JwtAuthorizationFilter过滤器是否有效!
     *
     * @param currentPrincipal 当事人信息
     * @return 发送响应, 返回一个当事人信息, 并接收
     */
    @PostMapping("/bcd")
    public RestBean<CurrentPrincipal> Test(@AuthenticationPrincipal CurrentPrincipal currentPrincipal) {

        return RestBean.success(currentPrincipal);
    }
}
