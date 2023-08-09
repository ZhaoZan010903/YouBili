package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.pojo;


import lombok.Data;

import java.io.Serializable;

@Data
public class VideoCount implements Serializable {

    private Long videoID;
    private Integer viewCount;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer dislikeCount;
    private Integer barrageCount;
    private Integer commentCount;
}
