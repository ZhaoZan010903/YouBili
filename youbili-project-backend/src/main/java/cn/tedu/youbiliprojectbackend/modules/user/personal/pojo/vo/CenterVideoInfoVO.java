package cn.tedu.youbiliprojectbackend.modules.user.personal.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CenterVideoInfoVO implements Serializable {
    private String nickname;
    private String imgUrl;
    private String videoImgUrl;
    private String title;
    private Long likeCount;
    private String categoryName;
    private Long commentCount;
    private String description;
    private String viewCount;
}
