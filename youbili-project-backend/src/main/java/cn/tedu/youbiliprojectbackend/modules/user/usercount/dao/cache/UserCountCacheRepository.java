package cn.tedu.youbiliprojectbackend.modules.user.usercount.dao.cache;

import cn.tedu.youbiliprojectbackend.common.consts.UserCacheConsts;

public interface UserCountCacheRepository extends UserCacheConsts {
    /**
     * 向redis之中的计数字段添加粉丝数+1;
     *
     * @param userID
     */
    void addFans(Long userID);

    /**
     * 向redis之中的计数字段粉丝数-1;
     *
     * @param userID
     */
    void removeFans(Long userID);

    /**
     * 向redis之中的计数字段关注数+1;
     *
     * @param userID
     */
    void addFollow(Long userID);


    /**
     * 向redis之中的计数字段粉丝数-1;
     *
     * @param userID
     */
    void removeFollow(Long userID);

    /**
     * 向redis之中的计数字段视频数+1;
     *
     * @param userID
     */
    void addVideoCount(Long userID);

    /**
     * 向redis之中的计数字段视频数-1;
     *
     * @param userID
     */
    void removeVideoCount(Long userID);


    /**
     * 向redis之中的计数字段总点赞数-1;
     *
     * @param userID
     */
    void addTotalCount(Long userID);


    /**
     * 向redis之中的计数字段总点赞数-1;
     *
     * @param userID
     */
    void removeTotalCount(Long userID);
}
