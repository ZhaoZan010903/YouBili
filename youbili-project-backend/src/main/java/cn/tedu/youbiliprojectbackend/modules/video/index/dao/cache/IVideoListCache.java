package cn.tedu.youbiliprojectbackend.modules.video.index.dao.cache;


import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.pojo.vo.UserCountsCacheVO;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.pojo.VideoCount;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.common.consts.CountConsts;
import cn.tedu.youbiliprojectbackend.common.consts.VideoConsts;


public interface IVideoListCache extends VideoConsts, CountConsts {
    /**
     * 查询视频计数字段
     *
     * @param videoID 视频ID
     * @return 视频计数字段
     */
    VideoCount listLike(Long videoID);

    /**
     * 查询视频的Url等
     *
     * @param videoID 视频ID
     * @return 视频url
     */
    VideoUserCache videoListselect(Long videoID);

    /**
     * 查询作者的关注等
     *
     * @param userID 作者ID
     * @return 作者计数字段
     */
    UserCountsCacheVO listFansFolCount(Long userID);
}
