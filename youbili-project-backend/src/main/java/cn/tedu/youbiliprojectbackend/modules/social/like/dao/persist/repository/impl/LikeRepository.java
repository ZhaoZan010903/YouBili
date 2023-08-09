package cn.tedu.youbiliprojectbackend.modules.social.like.dao.persist.repository.impl;

import cn.tedu.youbiliprojectbackend.modules.social.like.dao.persist.mapper.LikeMapper;
import cn.tedu.youbiliprojectbackend.modules.social.like.dao.persist.repository.ILikeRepository;
import cn.tedu.youbiliprojectbackend.modules.social.like.pojo.entity.Like;
import cn.tedu.youbiliprojectbackend.modules.social.like.pojo.vo.LikeTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class LikeRepository implements ILikeRepository {


    @Autowired
    private LikeMapper likeMapper;

    public LikeRepository() {
        log.info("开始处理点赞的持久层");
    }

    /**
     * 点赞
     *
     * @param likes
     * @return
     */
    @Override
    public int insert(Like likes) {
        int rows = likeMapper.insert(likes);
        return rows;
    }

    /**
     * 取消点赞
     *
     * @return
     */
    @Override
    public int delete(Long userID, Long videoID) {
        int rows = likeMapper.delete(userID, videoID);
        return rows;
    }

    @Override
    public LikeTypeVO selectTypeEnableByVideoID(Long userID, Long videoID) {
        return likeMapper.selectTypeEnableByVideoID(userID, videoID);
    }

}
