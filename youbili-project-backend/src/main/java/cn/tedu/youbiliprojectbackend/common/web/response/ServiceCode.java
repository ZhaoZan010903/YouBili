package cn.tedu.youbiliprojectbackend.common.web.response;

import lombok.Getter;

/**
 * 业务状态码
 *
 * @author java@tedu.cn
 * @version 1.0
 */
@Getter
public enum ServiceCode {

    /**
     * 操作成功
     */
    OK(20000, "操作成功"),
    FAILER(50000, "操作失败"),
    /**
     * 错误：请求参数格式错误
     */
    ERROR_BAD_REQUEST(40000, "请求参数格式错误"),
    /**
     * 错误: 未认证
     */
    ERROR_BUNAUTHORIZED(40100, "未认证"),
    /**
     * 错误：未认证，因为被禁用
     */
    ERROR_UNAUTHORIZED_DISABLED(40101, "被禁用"),
    /**
     * 错误: 禁止访问!无权限!
     */
    ERROR_FORBIDDEN(40300, "身份未认证"),
    /**
     * 错误：数据不存在
     */
    ERROR_NOT_FOUND(40400, "数据不存在"),
    /**
     * 错误：数据冲突
     */
    ERROR_CONFLICT(40900, "数据冲突"),
    /**
     * 账号禁用,被禁用
     */
    ERROR_USER_DISABLED(40910, "账户已经被禁用"),
    /**
     * 邮件服务问题!
     */
    ERROR_SERVER_MAIL(40920, "邮件服务系统忙!"),
    /**
     * JWT过期
     */
    ERR_JWT_EXPIRED(60000, "错误：JWT已过期"),
    /**
     * 错误：JWT验证签名失败，可能使用了伪造的JWT
     */
    ERR_JWT_SIGNATURE(60100, "JWT验证签名失败"),
    /**
     * 错误：JWT格式错误
     */
    ERR_JWT_MALFORMED(60200, "JWT格式错误"),
    /**
     * 错误：其它异常
     */
    ERROR_UNKNOWN(99999, "其它异常"),
    /**
     * 错误: 异常
     */
    ERROR(50000, "操作异常");

    private final Integer value;
    private final String message;

    ServiceCode(Integer value, String message) {
        this.value = value;
        this.message = message;
    }


}
