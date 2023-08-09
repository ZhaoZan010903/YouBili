package cn.tedu.youbiliprojectbackend.modules.social.notice.dao.persist.repository.impl;


import cn.tedu.youbiliprojectbackend.modules.social.notice.dao.persist.mapper.NoticeMapper;
import cn.tedu.youbiliprojectbackend.modules.social.notice.dao.persist.repository.INoticeRepository;
import cn.tedu.youbiliprojectbackend.modules.social.notice.pojo.vo.NoticeAllListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class NoticeRepositoryImpl implements INoticeRepository {

    @Autowired
    private NoticeMapper noticeMapper;

    public NoticeRepositoryImpl() {
        log.debug("通知的dao层");
    }

    @Override
    public List<NoticeAllListVO> userNoticeList(Integer enable) {
        List<NoticeAllListVO> list = noticeMapper.userEnableNoticeList(enable);
        return list;
    }

    @Override
    public List<NoticeAllListVO> adminNoticeList(Integer enable) {
        List<NoticeAllListVO> adminNoticeAllList = noticeMapper.adminEnableNoticeList(enable);
        return adminNoticeAllList;
    }

    @Override
    public List<NoticeAllListVO> userNoticeUserId(Long userID) {
        return noticeMapper.userNoticeUserId(userID);
    }


    @Override
    public void deleteByUserId(Long noticeID) {
        noticeMapper.deleteByUserId(noticeID);
    }
}
