package cn.tedu.youbiliprojectbackend.modules.user.account.controller;


import cn.tedu.youbiliprojectbackend.common.consts.HttpConsts;
import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.param.UserLoginInfoParam;
import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.vo.AccountMeVO;
import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.vo.UserLoginVO;
import cn.tedu.youbiliprojectbackend.modules.user.account.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@Validated
public class AccountController implements HttpConsts {
    @Autowired
    private IUserService userService;


    AccountController() {
        log.debug("创建控制器: AccountController");
    }

    @PostMapping("/login")
    public RestBean<UserLoginVO> login(@Validated UserLoginInfoParam userLoginInfoParam,
                                       HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String userAgent = request.getHeader(HEADER_USER_AGENT);
        log.error(userAgent);
        UserLoginVO userLoginResultVO = userService.login(userLoginInfoParam, remoteAddr, userAgent);
        return RestBean.success(userLoginResultVO);
    }


    /**
     * @param principal
     * @return
     */
    @GetMapping("/me")
    public RestBean<AccountMeVO> me(@AuthenticationPrincipal @NotNull(message = "未登录!") CurrentPrincipal principal) {

        return RestBean.success(userService.me(principal.getUserID()));
    }
}
