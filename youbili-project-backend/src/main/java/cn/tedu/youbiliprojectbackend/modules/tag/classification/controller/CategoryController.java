package cn.tedu.youbiliprojectbackend.modules.tag.classification.controller;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryTitleVO;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类别控制器
 *
 * @author 万佳兴
 */
@Slf4j
@RestController
@RequestMapping("/category/select/")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    public CategoryController() {
        log.info("创建类别控制器:CategoryController");
    }

    @GetMapping("/all-list")
    public RestBean<List<CategoryTitleVO>> listAll() {
        log.debug("开始处理【查询类别】的请求");
        List<CategoryTitleVO> categoryTitleVOS = categoryService.listTile();
        return RestBean.success(categoryTitleVOS);
    }


    @GetMapping("/video-list")
    public RestBean<List<VideoUserCache>> listVideo(Long categoryID) {
        /**
         * 通过视频分类关联表 查出相关分类的视频ID
         * 拿到视频ID后,向缓存之中模糊查询此ID下视频的相信信息 类型为 VideoUserCache
         * 直接返回给前端
         */
        List<VideoUserCache> videoUserCaches = categoryService.selectVideo(categoryID);
        System.out.println(videoUserCaches);
        return RestBean.success(videoUserCaches);
    }
}
