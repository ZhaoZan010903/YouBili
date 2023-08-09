package cn.tedu.youbiliprojectbackend.common.cacheUtils.schedule;


import cn.tedu.youbiliprojectbackend.common.cacheUtils.classification.service.ClassificationService;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.service.IUserCountsService;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.service.VideoCountService;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.mahout.service.UserCFRunService;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.service.VideoListService;
import cn.tedu.youbiliprojectbackend.modules.search.service.IVideoSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 项目启动时
 * 将视频列表,视频计数字段,以及用户计数字段自动装入到Redis之中
 *
 * @author 赵錾
 */
@Slf4j
@Component
public class CacheSchedule implements ApplicationRunner {
    @Autowired
    VideoListService videoListService;

    @Autowired
    VideoCountService videoCountService;

    @Autowired
    IUserCountsService userCountsService;

    @Autowired
    ClassificationService classificationService;

    @Autowired
    UserCFRunService userCFRunService;

    @Autowired
    IVideoSearchService iVideoSearchService;

    /**
     * 当项目启动时
     * 运行下面的方法
     * 从数据库之中取出所有的视频列表
     * 将计数字段意外的其他信息全部存入缓存之中
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("开始向缓存中写入数据 ----- 视频列表!");
        videoListService.saveCacheVideo();
        log.debug("开始向缓存中写入数据 ----- 视频计数字段");
        videoCountService.SaveCacheVideoCount();
        log.debug("开始向缓存中写入数据 ----- 用户计数字段");
        userCountsService.saveCacheUserCounts();
        log.debug("开始向缓存中写入数据 ----- 分类列表");
        classificationService.CacheClassification();
        log.debug("开始向缓存中写入数据 ----- 推荐数据");
        userCFRunService.UserCFRun();


        log.debug("开始写入搜索数据 ----- ");
        iVideoSearchService.rebuildSearch();
    }

//    @Scheduled(cron = "0 0/2 * * * ?")
//    public void updata(){
//        log.debug("开始向缓存中写入数据 ----- 视频列表!");
//        videoListService.saveCacheVideo();
//        log.debug("开始向缓存中写入数据 ----- 视频计数字段");
//        videoCountService.SaveCacheVideoCount();
//        log.debug("开始向缓存中写入数据 ----- 用户计数字段");
//        userCountsService.saveCacheUserCounts();
//        log.debug("开始向缓存中写入数据 ----- 分类列表");
//        classificationService.CacheClassification();
//        log.debug("开始向缓存中写入数据 ----- 推荐数据");
//        userCFRunService.UserCFRun();
//    }


}
