package cn.tedu.youbiliprojectbackend.modules.social.notice.service;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.modules.social.notice.pojo.vo.NoticeAllListVO;

import java.util.List;


public interface INoticeService {
    List<NoticeAllListVO> userNoticeList(NoticeAllListVO noticeAllListVOS, CurrentPrincipal principal);

    void deleteByUserId(Long noticeID);

}
