package cn.tedu.youbiliprojectbackend.modules.social.notice.service.impl;

import cn.tedu.youbiliprojectbackend.common.ex.ServiceException;
import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.social.notice.dao.persist.repository.INoticeRepository;
import cn.tedu.youbiliprojectbackend.modules.social.notice.pojo.vo.NoticeAllListVO;
import cn.tedu.youbiliprojectbackend.modules.social.notice.service.INoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NoticeServiceImpl implements INoticeService {

    @Autowired
    private INoticeRepository noticeRepository;


    @Override
    public List<NoticeAllListVO> userNoticeList(NoticeAllListVO noticeAllListVOS, CurrentPrincipal principal) {
        Integer userEnable = noticeAllListVOS.getTypeEnable();
        Long userNoticeUserId = principal.getUserID();
        List<NoticeAllListVO> list = new ArrayList<>();
        if (userEnable == 1) {
            List<NoticeAllListVO> noticePrivateList = noticeRepository.userNoticeUserId(userNoticeUserId);
            for (NoticeAllListVO noticePrivate : noticePrivateList) {
                Long userID = noticePrivate.getUserID();
                if (userNoticeUserId == userID) {
                    list.add(noticePrivate);
                }
            }
            return list;
        } else if (userEnable == 0) {
            log.debug("开始处理【查询系统通知】的业务");
            List<NoticeAllListVO> adminNoticeAllList = noticeRepository.adminNoticeList(userEnable);
            return adminNoticeAllList;
        } else {
            return null;
        }
    }

    @Override
    public void deleteByUserId(Long noticeID) {
        if (noticeID == null) {
            String message = "删除的问内容不存在";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_BAD_REQUEST, message);
        }
        noticeRepository.deleteByUserId(noticeID);
    }
}
