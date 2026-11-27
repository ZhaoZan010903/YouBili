package com.youbili.common.response;

import lombok.Getter;

@Getter
public enum ServiceCode {

    OK(20000, "操作成功"),
    BAD_REQUEST(40000, "请求参数错误"),
    UNAUTHORIZED(40100, "请先登录后再操作"),
    USER_DISABLED(40101, "账号已被禁用"),
    FORBIDDEN(40300, "无权访问该资源"),
    NOT_FOUND(40400, "请求的资源不存在"),
    CONFLICT(40900, "数据已存在或状态冲突"),
    TOO_MANY_REQUESTS(42900, "请求过于频繁，请稍后再试"),
    INTERNAL_ERROR(50000, "服务器内部异常，请稍后再试"),
    MAIL_ERROR(50010, "邮件发送失败，请检查邮件配置"),
    STORAGE_ERROR(50020, "文件存储服务异常"),
    PAYMENT_ERROR(50030, "支付服务异常"),
    JWT_EXPIRED(60000, "登录状态已过期，请重新登录"),
    JWT_INVALID(60100, "无效的登录凭证");

    private final int code;
    private final String message;

    ServiceCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
