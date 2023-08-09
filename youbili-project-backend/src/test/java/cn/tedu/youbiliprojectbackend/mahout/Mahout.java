package cn.tedu.youbiliprojectbackend.mahout;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.dao.cache.UserCFCache;

import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.score.impl.MahoutUserCFImpl;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.service.UserCFService;
import cn.tedu.youbiliprojectbackend.modules.video.index.controller.videoController;
import com.alibaba.fastjson.JSONObject;
import org.apache.mahout.cf.taste.common.TasteException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Mahout {
    @Autowired
    MahoutUserCFImpl mahoutUserCF;

    @Autowired
    UserCFCache userCFCache;

    @Autowired
    UserCFService userCFService;

    @Test
    void UserCF() throws TasteException {
        List<Long> recommend = mahoutUserCF.recommend(5L);
        System.out.println(recommend);
    }

    @Test
    void Cache() throws TasteException {
        List<VideoUserCache> videoUserCaches = userCFService.UserCF(0L, 4);
        System.out.println(videoUserCaches.toString());

    }

    @Autowired
    videoController videoController;

    @Test
    void tagList() {
        System.out.println(JSONObject.toJSONString(videoController.tagList(38L)));
    }

}
