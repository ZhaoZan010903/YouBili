package cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FanListVO implements Serializable {
    private Long followID;
    private Long followerID;
    private String nickname;
    private Integer fansCount;
    private Integer followCount;
    private String imgUrl;
}
