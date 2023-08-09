package cn.tedu.youbiliprojectbackend.modules.user.register.controller;

import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.user.register.pojo.dto.AccountRegDTO;
import cn.tedu.youbiliprojectbackend.modules.user.register.service.AuthorizeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 邮箱验证注册功能
 *
 * @author 赵錾
 * @date 2023年6月7日15:56:19
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    @Autowired
    AuthorizeService service;


    AuthorizeController() {
        log.info("创建控制器对象：AuthorizeController");
    }

    /**
     * 发送验证邮件
     * 验证email是否符合正则表达式,若不符合则报错
     *
     * @param email
     * @return
     */
    @PostMapping("/vaild-email")
    public RestBean<Object> vaildateEmail(@Email @RequestParam("email") String email,
                                          HttpSession session) {
        String sessionId = session.getId();
        service.sendVaildateEmail(email, sessionId);
        return RestBean.success();
    }


    /**
     * 注册验证
     *
     * @param accountRegDTO
     * @return
     */
    @PostMapping("/register")
    public RestBean<String> registerUser(@Validated AccountRegDTO accountRegDTO, HttpSession session) {
        String sessionId = session.getId();
        log.debug(accountRegDTO.toString());
        service.validateAndRegister(accountRegDTO.getUsername(), accountRegDTO.getPassword(), accountRegDTO.getEmail(), accountRegDTO.getNickname(), accountRegDTO.getCode(), sessionId);
        return RestBean.success("注册成功");
    }
}
