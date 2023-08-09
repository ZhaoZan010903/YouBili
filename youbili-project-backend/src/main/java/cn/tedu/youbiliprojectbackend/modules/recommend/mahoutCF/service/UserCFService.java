package cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.service;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;

import java.util.List;

/**
 * 基于用户的协同过滤算法
 *
 * @author 赵錾
 */
public interface UserCFService {
    List<VideoUserCache> UserCF(Long userID, Integer num);


}
