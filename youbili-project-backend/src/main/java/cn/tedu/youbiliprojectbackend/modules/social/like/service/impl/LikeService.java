package cn.tedu.youbiliprojectbackend.modules.social.like.service.impl;


import cn.tedu.youbiliprojectbackend.common.ex.ServiceException;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.social.like.dao.persist.repository.ILikeRepository;
import cn.tedu.youbiliprojectbackend.modules.social.like.pojo.entity.Like;
import cn.tedu.youbiliprojectbackend.modules.social.like.pojo.vo.LikeTypeVO;
import cn.tedu.youbiliprojectbackend.modules.social.like.service.ILikeService;
import cn.tedu.youbiliprojectbackend.modules.video.videocount.dao.cache.VideoCountCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * 点赞的业务层实现类
 *
 * @author 万佳兴
 */
@Slf4j
@Service
public class LikeService implements ILikeService {

    @Autowired
    private ILikeRepository likeRepository;
    @Autowired
    private VideoCountCacheRepository cacheRepository;

    @Override
    public void addLike(Long userID, Long videoID) {
        System.out.println("userID = " + userID + ", param = " + videoID);


        LikeTypeVO i = likeRepository.selectTypeEnableByVideoID(userID, videoID);
        if (i != null && i.getTypeEnable() == 0) {
            throw new ServiceException(ServiceCode.ERROR, "点赞点踩不能同时进行!");
        }
        if (i == null) {
            Like like = new Like();
            like.setVideoID(videoID);
            like.setTypeEnable(1);
            like.setUserID(userID);
            like.setLikeDate(LocalDateTime.now());
            like.setGmtCreate(LocalDateTime.now());
            likeRepository.insert(like);
            cacheRepository.addLike(videoID);
        }

    }

    @Override
    public void removeLike(Long userID, Long videoID) {
        likeRepository.delete(userID, videoID);
        cacheRepository.removeLike(videoID);
    }

    @Override
    public void addDislike(Long userID, Long videoID) {
        log.debug("{}", videoID);
        LikeTypeVO i = likeRepository.selectTypeEnableByVideoID(userID, videoID);
        if (i != null && i.getTypeEnable() == 1) {
            throw new ServiceException(ServiceCode.ERROR, "点赞点踩不能同时进行!");
        }
        if (i == null) {
            Like like = new Like();
            like.setVideoID(videoID);
            like.setTypeEnable(0);
            like.setUserID(userID);
            like.setLikeDate(LocalDateTime.now());
            like.setGmtCreate(LocalDateTime.now());
            likeRepository.insert(like);
            cacheRepository.addDisLike(videoID);
        }


    }

    @Override
    public void removeDislike(Long userID, Long videoID) {
        likeRepository.delete(userID, videoID);
        cacheRepository.removeLike(videoID);
    }

    @Override
    public LikeTypeVO getTypeEnable(Long userID, Long videoID) {
        return likeRepository.selectTypeEnableByVideoID(userID, videoID);
    }
}
