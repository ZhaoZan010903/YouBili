package cn.tedu.youbiliprojectbackend.modules.user.history.pojo.param;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class HistoryParam implements Serializable {

    /**
     * 历史记录ID
     */
    private Long historyID;
    /**
     * 视频作者ID
     */
    private Long userID;
    /**
     * 视频ID
     */
    private Long videoID;
    /**
     * 视频封面
     */
    private String videoImgUrl;
    /**
     * 作者的头像
     */
    private String imgUrl;
    /**
     * 视频的标题
     */
    private String title;
    /**
     * 作者的名称
     */
    private String nickname;
    /**
     * 显示状态，0=不显示，1=显示
     */
    private Integer enable;
    /**
     * 观看日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime watchDate;
    /**
     * 观看时长
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalDateTime watchDuration;
}
