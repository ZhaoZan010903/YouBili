package cn.tedu.youbiliprojectbackend.modules.user.favorite.service;


import cn.tedu.youbiliprojectbackend.modules.user.favorite.pojo.vo.FavoriteVO;

import java.util.List;

/**
 * @author ASUS
 */
public interface IFavoriteService {

    /**
     * 获取收藏视频列表
     *
     * @param userID 当事人ID
     * @return 返回列表数据
     */
    List<FavoriteVO> list(Long userID);

    /**
     * 删除收藏信息;
     *
     * @param favoriteID
     * @param userID
     */
    void deleteById(Long favoriteID, Long userID, Long videoID);

    void insterFavorite(Long userID, Long videoID);

    void deleteByUserIDandVideoID(Long userID, Long videoID);
}
