package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.common.consts.VideoConsts;

import java.util.List;

public interface Select extends VideoConsts {
    List<VideoUserCache> Select();
}
