package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserCountsCacheVO implements Serializable {
    private Long userID;
    private Integer fans;
    private Integer follows;
    private Integer videoCount;
    private Integer totalLikes;
}
