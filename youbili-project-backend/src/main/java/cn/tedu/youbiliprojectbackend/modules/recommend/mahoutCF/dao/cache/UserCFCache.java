package cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.dao.cache;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.common.consts.MahoutConsts;
import cn.tedu.youbiliprojectbackend.common.consts.VideoConsts;
import org.apache.mahout.cf.taste.common.TasteException;

import java.util.List;

/**
 * 推荐视频计算出videoID以后从缓存里面拿
 */
public interface UserCFCache extends VideoConsts, MahoutConsts {
    List<VideoUserCache> CFvideoList(Long userID) throws TasteException;


}
