package cn.tedu.youbiliprojectbackend.modules.user.favorite.dao.persist.repository.impl;

import cn.tedu.youbiliprojectbackend.modules.user.favorite.dao.persist.mapper.FavoriteMapper;
import cn.tedu.youbiliprojectbackend.modules.user.favorite.dao.persist.repository.IFavoriteRepository;
import cn.tedu.youbiliprojectbackend.modules.user.favorite.pojo.entity.Favorite;
import cn.tedu.youbiliprojectbackend.modules.user.favorite.pojo.vo.FavoriteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class FavoriteRepositroy implements IFavoriteRepository {

    @Autowired
    FavoriteMapper favoriteMapper;


    /**
     * 收藏列表接口
     *
     * @param userID 当事人ID
     * @return 查询出来的收藏列表
     */
    @Override
    public List<FavoriteVO> list(Long userID) {

        return favoriteMapper.selectListById(userID);
    }

    /**
     * 删除收藏视频
     *
     * @param favoriteID
     */
    @Override
    public int deleteById(Long userID, Long favoriteID) {
        int i = favoriteMapper.deleteByFavoriteIDAndUserID(userID, favoriteID);
        return i;
    }


    /**
     * 添加收藏
     *
     * @param userID  当事人ID
     * @param videoID 视频ID
     * @return 返回添加行数
     */
    @Override
    public int addFavorite(Long userID, Long videoID) {
        if (favoriteMapper.selectByEnable(userID, videoID) != null) {
            return 0;
        }
        return favoriteMapper.insertAll(userID, videoID);
    }

    @Override
    public int deleteByuserIDAndvideoID(Long userID, Long videoID) {
        int i = favoriteMapper.deleteByuserIDandvideoID(userID, videoID);
        return i;
    }

    @Override
    public Boolean selectByEnable(Long userID, Long videoID) {
        Favorite favorite = favoriteMapper.selectByEnable(userID, videoID);

        if (favorite == null) {
            log.debug("null ----- ");
            return false;
        }
        return true;
    }


}
