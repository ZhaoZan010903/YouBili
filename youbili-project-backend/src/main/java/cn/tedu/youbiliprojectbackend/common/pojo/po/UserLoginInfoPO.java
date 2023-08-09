package cn.tedu.youbiliprojectbackend.common.pojo.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户登录信息的PO，此类数据将存入到Redis中
 *
 * @author java@tedu.cn
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class UserLoginInfoPO implements Serializable {

    /**
     * 用户登录时使用的浏览器的信息
     */
    private String userAgent;
    /**
     * 用户登录时的IP地址
     */
    private String ip;
}
