package cn.tedu.youbiliprojectbackend.modules.social.like.dao.persist.repository;

import cn.tedu.youbiliprojectbackend.modules.social.like.pojo.entity.Like;
import cn.tedu.youbiliprojectbackend.modules.social.like.pojo.vo.LikeTypeVO;

public interface ILikeRepository {
    /**
     * 插入点赞数据
     *
     * @param likes
     * @return
     */
    int insert(Like likes);

    /**
     * 取消点赞
     *
     * @return
     */
    int delete(Long userID, Long videoID);

    LikeTypeVO selectTypeEnableByVideoID(Long userID, Long videoID);


}
