package cn.tedu.youbiliprojectbackend.modules.search.schedule;

import cn.tedu.youbiliprojectbackend.modules.search.service.IVideoSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
//@Component
public class VideoSearchSchedule {
    @Autowired
    IVideoSearchService videoSearchService;

    @Scheduled(fixedRate = 10 * 1000)
    public void rebuildSearch() {
        log.debug("更新elasticsearch中的视频列表数据");
        videoSearchService.rebuildSearch();
    }
}
