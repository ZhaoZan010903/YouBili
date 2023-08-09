package cn.tedu.youbiliprojectbackend.modules.social.notice.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("notice")
public class Notice implements Serializable {

    private Long noticeID;

    private Long userID;

    private String systemName;

    private Integer typeEnable;

    private String noticeText;

    private Date noticeTime;

    private Date gmtCreate;

    private Date gmtModified;
}
