package cn.tedu.youbiliprojectbackend.modules.search.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class VideoSearchVO implements Serializable {
    /**
     * 视频ID
     */
    private Long videoID;
    /**
     * 用户ID
     */
    private Long userID;
    /**
     * 标题
     */
    private String title;
    /**
     * 视频描述
     */
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
    /**
     * 视频源地址
     */
    private String videoSrcUrl;
    /**
     * 封面地址
     */
    private String videoImgUrl;
    /**
     * 作者昵称
     */
    private String nickname;
    /**
     * 作者头像地址
     */
    private String imgUrl;
}
