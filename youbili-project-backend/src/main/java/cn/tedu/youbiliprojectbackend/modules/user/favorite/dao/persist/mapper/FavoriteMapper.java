package cn.tedu.youbiliprojectbackend.modules.user.favorite.dao.persist.mapper;

import cn.tedu.youbiliprojectbackend.modules.user.favorite.pojo.entity.Favorite;
import cn.tedu.youbiliprojectbackend.modules.user.favorite.pojo.vo.FavoriteVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ASUS
 */
@Repository
public interface FavoriteMapper extends BaseMapper<Favorite> {

    List<FavoriteVO> selectListById(Long userID);

    int deleteByFavoriteIDAndUserID(Long userID, Long favoriteID);

    int insertAll(Long userID, Long videoID);

    int deleteByuserIDandvideoID(Long userID, Long videoID);

    Favorite selectByEnable(Long userID, Long videoID);
}
