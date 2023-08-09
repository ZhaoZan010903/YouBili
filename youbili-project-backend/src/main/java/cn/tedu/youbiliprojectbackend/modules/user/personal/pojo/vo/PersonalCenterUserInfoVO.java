package cn.tedu.youbiliprojectbackend.modules.user.personal.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PersonalCenterUserInfoVO implements Serializable {
    private String imgUrl;
    private String nickname;
    private Long fans;//粉丝数量
    private Long follows; //关注数量;
    private Long totalLikes;//总点赞数
    private String birthday;//生日
    private Long accountLevel;//账户等级
}
