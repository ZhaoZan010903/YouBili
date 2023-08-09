package cn.tedu.youbiliprojectbackend.modules.social.notice.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NoticeAllListVO implements Serializable {

    private Long noticeID;

    private Long userID;

    private Long videoID;

    private String systemName;

    private Integer typeEnable;

    private String noticeText;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date noticeTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;

}
