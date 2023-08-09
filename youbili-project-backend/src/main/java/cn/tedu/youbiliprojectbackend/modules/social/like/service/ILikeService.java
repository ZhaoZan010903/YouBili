package cn.tedu.youbiliprojectbackend.modules.social.like.service;

import cn.tedu.youbiliprojectbackend.modules.social.like.pojo.vo.LikeTypeVO;

/**
 * 点赞的业务层接口
 *
 * @author 万佳兴
 */
public interface ILikeService {

    void addLike(Long userID, Long videoID);

    void removeLike(Long userID, Long videoID);

    void addDislike(Long userID, Long videoID);

    void removeDislike(Long userID, Long videoID);

    LikeTypeVO getTypeEnable(Long userID, Long videoID);
}
