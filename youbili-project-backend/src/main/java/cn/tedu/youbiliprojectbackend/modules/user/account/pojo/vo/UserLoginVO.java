package cn.tedu.youbiliprojectbackend.modules.user.account.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @author ASUS
 */
@Data
@Accessors(chain = true)
public class UserLoginVO implements Serializable {
    /**
     * 用户ID
     */
    private Long userID;
    /**
     * 用户名
     */
    private String username;
    private String nickname;
    private Integer fans;
    private Integer follows;
    private Integer accountLevel;
    /**
     * 头像
     */
    private String imgUrl;
    /**
     * 状态
     */
    private Integer enable;
    /**
     * Token（JWT）
     */
    private String token;
}
