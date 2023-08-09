package cn.tedu.youbiliprojectbackend.modules.user.follow.dao.persist.mapper;

import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.entity.Follow;
import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.entity.followedID;
import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo.FollowListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowMapper extends BaseMapper<Follow> {

    /**
     * 查询关注列表
     *
     * @param userID 当事人ID
     * @return
     */
    List<FollowListVO> selectListByUserId(Long userID);

    /**
     * 取消关注
     *
     * @param userID   当事人ID
     * @param followID 关注表的ID
     * @return
     */
    int delete(Long userID, Long followID);

    /**
     * 查询当事人是否关注了用户
     *
     * @param followerID 从当事人获取的ID
     * @param followedID 被关注人的ID
     * @return
     */
    int selectByFollowerIDAndFollowedID(Long followerID, Long followedID);

    /**
     * @param userID   当事人ID
     * @param followID 关注表的ID
     * @return
     */
    followedID selectByUserIDAndFollowID(Long userID, Long followID);

    Long selectByFollowId(Long followID);

    int insertAll(Long followerID, Long followedID);
}
