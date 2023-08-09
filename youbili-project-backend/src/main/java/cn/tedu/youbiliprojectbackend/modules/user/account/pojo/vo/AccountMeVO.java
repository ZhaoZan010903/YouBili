package cn.tedu.youbiliprojectbackend.modules.user.account.pojo.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * 用于登录成功之后获取到信息!
 *
 * @author ASUS
 */
@Data
public class AccountMeVO implements Serializable {
    private String nickname;
    private Integer fans;
    private Integer follows;
    private String imgUrl;
    private Integer accountLevel;

}
