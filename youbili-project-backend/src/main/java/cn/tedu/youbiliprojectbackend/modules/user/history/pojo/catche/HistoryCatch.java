package cn.tedu.youbiliprojectbackend.modules.user.history.pojo.catche;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

@Data
public class HistoryCatch implements Serializable {
    /**
     * 视频作者ID
     */
    private Long userID;
    /**
     * 视频ID
     */
    private Long videoID;

    /**
     * 显示状态，0=不显示，1=显示
     */
    private Integer enable;
    /**
     * 观看日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String watchDate;
    /**
     * 观看时长
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private String watchDuration;


}
