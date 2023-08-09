package cn.tedu.youbiliprojectbackend.modules.social.dynamic.controller;

import cn.tedu.youbiliprojectbackend.common.security.CurrentPrincipal;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.social.dynamic.pojo.vo.DynamicListVO;
import cn.tedu.youbiliprojectbackend.modules.social.dynamic.service.IDynameicService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author java@tedu.cn
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("")
public class DynamicController {

    @Autowired
    private IDynameicService dynameicService;

    public DynamicController() {
        log.debug("创建控制器对象:DynamicController");
    }

    @ApiOperation("获取动态列表")
    @GetMapping("Social/dynamic/dy-list")
    public RestBean<List<DynamicListVO>> list(@AuthenticationPrincipal
                                              CurrentPrincipal principal) {
        log.debug("开始处理获取动态列表请求 : {}", principal.getUsername());
        Long userID = principal.getUserID();
        List<DynamicListVO> list = dynameicService.list(userID);
        return RestBean.success(list);
    }
}
