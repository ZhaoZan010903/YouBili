package cn.tedu.youbiliprojectbackend.modules.video.index.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class videoListVO implements Serializable {
    private Long videoID;
    private Long userID;
    private String title;
    private String description;
    /**
     * 视频时长
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private Date duration;
    /**
     * 上传日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadDate;
    private String videoSrcUrl;
    private String videoImgUrl;
    private String nickname;
    private String imgUrl;

    /**
     * 总点赞数
     */
    private Integer likeCount;
    private Integer viewCount;
    private Integer favoriteCount;
    private Integer dislikeCount;
    private Integer barrageCount;
    private Integer commentCount;

    /**
     * 作者的点赞总数
     */
    private Integer fans;
    private Integer follow;
}
