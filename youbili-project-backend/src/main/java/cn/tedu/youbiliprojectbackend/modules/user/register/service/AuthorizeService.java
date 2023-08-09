package cn.tedu.youbiliprojectbackend.modules.user.register.service;

import cn.tedu.youbiliprojectbackend.common.consts.RegisterConsts;

public interface AuthorizeService extends RegisterConsts {
    void sendVaildateEmail(String email, String sessionId);

    void validateAndRegister(String username, String password, String email, String nickname, String code, String sessionId);
}
