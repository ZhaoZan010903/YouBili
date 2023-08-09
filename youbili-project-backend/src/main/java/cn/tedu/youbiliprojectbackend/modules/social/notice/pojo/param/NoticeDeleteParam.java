package cn.tedu.youbiliprojectbackend.modules.social.notice.pojo.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class NoticeDeleteParam implements Serializable {

    private Long noticeID;

    private Long userID;

}
