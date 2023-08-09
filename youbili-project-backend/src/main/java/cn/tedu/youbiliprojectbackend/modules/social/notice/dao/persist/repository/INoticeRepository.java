package cn.tedu.youbiliprojectbackend.modules.social.notice.dao.persist.repository;

import cn.tedu.youbiliprojectbackend.modules.social.notice.pojo.vo.NoticeAllListVO;

import java.util.List;

public interface INoticeRepository {
    List<NoticeAllListVO> userNoticeList(Integer enable);

    List<NoticeAllListVO> adminNoticeList(Integer enable);

    List<NoticeAllListVO> userNoticeUserId(Long userID);

    void deleteByUserId(Long noticeID);
}
