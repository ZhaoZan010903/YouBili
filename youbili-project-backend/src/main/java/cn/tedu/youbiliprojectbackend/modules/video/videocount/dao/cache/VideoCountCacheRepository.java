package cn.tedu.youbiliprojectbackend.modules.video.videocount.dao.cache;

import cn.tedu.youbiliprojectbackend.common.consts.UserCacheConsts;

/**
 * @author 万佳兴
 */
public interface VideoCountCacheRepository extends UserCacheConsts {
    /**
     * 向Redis之中的观看次数计数字段+1
     *
     * @param videoID
     */
    void addView(Long videoID);


    /**
     * 向Redis之中的点赞计数次数字段+1
     *
     * @param videoID
     */
    void addLike(Long videoID);

    /**
     * 向Redis之中的点赞计数次数字段-1
     *
     * @param videoID
     */
    void removeLike(Long videoID);

    /**
     * 向Redis之中的点踩计数次数字段+1
     */
    void addDisLike(Long videoID);

    /**
     * 向Redis之中的点踩计数次数字段+1
     */
    void removeDisLike(Long videoID);

    /**
     * 向Redis之中的弹幕计数次数字段+1
     */
    void addBarrageCount(Long videoID);

    /**
     * 向Redis之中的弹幕计数次数字段-1
     */
    void removeBarrageCount(Long videoID);

    /**
     * 向Redis之中的评论计数次数字段+1
     */
    void addCommentCount(Long videoID);

    /**
     * 向Redis之中的评论计数次数字段-1
     */
    void removeCommentCount(Long videoID);

    /**
     * 向Redis之中的评论计数次数字段+1
     */
    void addFavoriteCount(Long videoID);

    /**
     * 向Redis之中的评论计数次数字段-1
     */
    void removeFavoriteCount(Long videoID);

}
