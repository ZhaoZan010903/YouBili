package cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FollowListVO implements Serializable {
    private Long followID;
    private Long followedID;
    private String nickname;
    private Integer fansCount;
    private Integer followCount;
    private String imgUrl;
}
