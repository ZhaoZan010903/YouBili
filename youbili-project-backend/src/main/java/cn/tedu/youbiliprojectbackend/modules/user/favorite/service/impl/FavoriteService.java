package cn.tedu.youbiliprojectbackend.modules.user.favorite.service.impl;

import cn.tedu.youbiliprojectbackend.common.ex.ServiceException;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.user.favorite.dao.persist.repository.IFavoriteRepository;
import cn.tedu.youbiliprojectbackend.modules.user.favorite.pojo.vo.FavoriteVO;
import cn.tedu.youbiliprojectbackend.modules.user.favorite.service.IFavoriteService;
import cn.tedu.youbiliprojectbackend.modules.video.videocount.dao.cache.VideoCountCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FavoriteService implements IFavoriteService {

    @Autowired
    IFavoriteRepository favoriteRepository;
    @Autowired
    private VideoCountCacheRepository videoCountCacheRepository;

    @Override
    public List<FavoriteVO> list(Long userID) {

        List<FavoriteVO> list = null;
        try {
            list = favoriteRepository.list(userID);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (list == null) {
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, "没有查询到响应结果");
        }

        return list;

    }

    /**
     * 删除收藏信息;
     *
     * @param favoriteID
     * @param userID
     */
    @Override
    public void deleteById(Long favoriteID, Long userID, Long videoID) {
        int i = favoriteRepository.deleteById(userID, favoriteID);
        if (i == 0) {
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, "信息错误!请以正确的形式输入信息");
        }
        videoCountCacheRepository.removeFavoriteCount(videoID);
    }

    @Override
    public void insterFavorite(Long userID, Long videoID) {
        if (favoriteRepository.selectByEnable(userID, videoID)) {
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, "您以添加收藏！！！请勿重复添加");
        }
        int i = favoriteRepository.addFavorite(userID, videoID);
        if (i != 1) {
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, "信息错误!请以正确的形式输入信息");
        }
        videoCountCacheRepository.addFavoriteCount(videoID);
    }

    @Override
    public void deleteByUserIDandVideoID(Long userID, Long videoID) {
        if (favoriteRepository.selectByEnable(userID, videoID)) {
            int i = favoriteRepository.deleteByuserIDAndvideoID(userID, videoID);
            log.debug("{}", i);
            if (i > 0) {
                videoCountCacheRepository.removeFavoriteCount(videoID);
            } else {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "删除错误");
            }
        } else {
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, "删除错误");
        }
    }

}
