package cn.tedu.youbiliprojectbackend.modules.user.history.controller;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.user.history.pojo.vo.HistoryListVO;
import cn.tedu.youbiliprojectbackend.modules.user.history.service.IHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 历史记录功能
 *
 * @author 杨影茹
 */
@Slf4j
@RestController
@RequestMapping("/user/history")
@Validated
public class historyController {
    @Autowired
    IHistoryService historyService;


    historyController() {
        log.debug("创建控制器类: historyController");
    }

    /**
     * 为什么history表之中要用enable来表示删除与否:
     * 因为要通过历史记录表统计观看量 :
     */

    /**
     * 加载历史记录列表
     *
     * @param principal 当事人信息
     * @return 返回响应结果
     */
    @GetMapping("/list")
    public RestBean<Object> list(@AuthenticationPrincipal CurrentPrincipal principal) {
        log.debug("开始接收请求");
        Long userID = principal.getUserID();
        List<HistoryListVO> list = historyService.list(userID);
        return RestBean.success(list);
    }

    /**
     * 删除历史记录
     *
     * @param principal 当事人信息
     * @param historyID 历史记录ID
     * @return 返回响应结果
     */
    @PostMapping("/delete-by-videoId")
    public RestBean<Object> delete(@AuthenticationPrincipal CurrentPrincipal principal, Long historyID) {
        log.debug("开始删除此历史记录");
        log.debug("{}", historyID);
        historyService.deleteByHistoryId(principal.getUserID(), historyID);
        System.out.println(historyID);
        return RestBean.success("删除历史记录成功");
    }

    /**
     * 添加历史记录
     * 考虑问题 :
     * 1. 历史记录更新时间戳问题 ;
     * 2. 删除历史记录的情况下,在添加如何解决 ;
     * 3. 已存在的历史记录如何添加 ;
     *
     * @param currentPrincipal 当事人ID
     * @param videoID          视频ID
     * @return 返回
     */
    @GetMapping("/addHistory")
    public RestBean<String> add(@AuthenticationPrincipal CurrentPrincipal currentPrincipal, Long videoID, LocalDateTime watchDuration) {
        log.debug("开始向缓存中添加历史记录");
        historyService.insert(currentPrincipal.getUserID(), videoID, watchDuration);
        return RestBean.success("添加历史记录成功");
    }

    public RestBean<String> add(Long userID, Long videoID, LocalDateTime watchDuration, LocalDateTime watchDate) {
        log.debug("开始向数据库添加历史记录");
        historyService.add(userID, videoID, watchDuration, watchDate);
        return RestBean.success("添加历史记录成功");
    }
}
