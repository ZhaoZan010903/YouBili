package cn.tedu.youbiliprojectbackend.modules.social.notice.controller;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.social.notice.pojo.param.NoticeDeleteParam;
import cn.tedu.youbiliprojectbackend.modules.social.notice.pojo.vo.NoticeAllListVO;
import cn.tedu.youbiliprojectbackend.modules.social.notice.service.INoticeService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/social/notice")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;


    /**
     * 通知公告分开 ;
     * 通知的内容需要获取到-userID,以及enable;
     * 公告的内容则需要一个enable == 0;
     *
     * @param principal
     * @param enable
     * @return
     */
    @GetMapping("/listSelect")
    @ApiOperation("用户通知")
    @ApiOperationSupport(order = 100)
    public RestBean<List<NoticeAllListVO>> getSelectNotice(@AuthenticationPrincipal CurrentPrincipal principal, Integer enable) {
        log.debug("开始处理【查询通知】的请求");
        System.out.println("当事人ID" + principal.getUserID());
        NoticeAllListVO noticeAllListVO = new NoticeAllListVO();
        noticeAllListVO.setTypeEnable(enable);
        List<NoticeAllListVO> noticeAllListVOPageData = noticeService.userNoticeList(noticeAllListVO, principal);
        log.debug(String.valueOf(noticeAllListVO));
        log.debug("noticeAllListVOPageData{}", noticeAllListVOPageData);
        return RestBean.success(noticeAllListVOPageData);
    }


    @PostMapping("/notifyDelete")
    @ApiOperation("根据noticeID删除个人通知")
    public RestBean<NoticeDeleteParam> deleteByUserId(@AuthenticationPrincipal CurrentPrincipal principal, Long noticeID) {
        log.debug("noticeID:{}", noticeID);
        noticeService.deleteByUserId(noticeID);
        return RestBean.success();
    }
}
