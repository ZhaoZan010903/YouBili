package cn.tedu.youbiliprojectbackend.modules.user.favorite.dao.persist.repository;


import cn.tedu.youbiliprojectbackend.modules.user.favorite.pojo.vo.FavoriteVO;

import java.util.List;

public interface IFavoriteRepository {
    /**
     * 收藏列表接口
     *
     * @param userID 当事人ID
     * @return 查询出来的收藏列表
     */
    List<FavoriteVO> list(Long userID);

    /**
     * 删除收藏视频
     *
     * @param userID
     * @param favoriteID 收藏ID
     * @return 返回删除行数
     */
    int deleteById(Long favoriteID, Long userID);

    /**
     * 添加收藏
     *
     * @param userID  当事人ID
     * @param videoID 视频ID
     * @return 返回添加行数
     */
    int addFavorite(Long userID, Long videoID);

    int deleteByuserIDAndvideoID(Long userID, Long videoID);

    Boolean selectByEnable(Long userID, Long videoID);
}
