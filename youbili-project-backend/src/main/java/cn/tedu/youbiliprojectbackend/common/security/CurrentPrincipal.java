package cn.tedu.youbiliprojectbackend.common.security;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于Security框架的当前认证信息中的当事人
 *
 * @author java@tedu.cn
 * @version 1.0
 */
@Data
public class CurrentPrincipal implements Serializable {

    /**
     * 当事人ID
     */
    private Long userID;
    /**
     * 当事人用户名
     */
    private String username;

}
