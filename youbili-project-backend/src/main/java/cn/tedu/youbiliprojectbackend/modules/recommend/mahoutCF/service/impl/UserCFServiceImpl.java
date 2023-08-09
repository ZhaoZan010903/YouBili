package cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.service.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.common.ex.ServiceException;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.dao.cache.UserCFCache;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.service.UserCFService;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 基于用户的协同过滤算法
 *
 * @author 赵錾
 */
@Service
@Slf4j
public class UserCFServiceImpl implements UserCFService {

    @Autowired
    UserCFCache userCFCache;

    /**
     * 登录状态下
     *
     * @param userID 用户ID
     * @param num
     * @return 视频集合
     */
    @Override
    public List<VideoUserCache> UserCF(Long userID, Integer num) {
        List<VideoUserCache> videoUserCacheList = null;
        try {
            videoUserCacheList = userCFCache.CFvideoList(userID);
        } catch (TasteException e) {
            throw new ServiceException(ServiceCode.ERROR_UNKNOWN, "系统内部错误,请联系系统管理员");
        }
        while (videoUserCacheList.size() > num) {
            int i = 1;
            i++;
            videoUserCacheList.remove(videoUserCacheList.size() - i);
        }

        return videoUserCacheList;
    }


}
