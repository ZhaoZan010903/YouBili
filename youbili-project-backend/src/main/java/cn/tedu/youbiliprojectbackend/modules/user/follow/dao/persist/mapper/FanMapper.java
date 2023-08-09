package cn.tedu.youbiliprojectbackend.modules.user.follow.dao.persist.mapper;

import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.entity.Follow;
import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo.FanListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FanMapper extends BaseMapper<Follow> {

    /**
     * 查询粉丝列表
     *
     * @param userID 当事人ID
     * @return
     */
    List<FanListVO> selectListByUserId(Long userID);

    /**
     * 删除粉丝
     *
     * @param userID   当事人ID
     * @param followID 关注表的ID
     * @return
     */
    int delete(Long userID, Long followID);

    /**
     * 查询用户是否有这个粉丝
     *
     * @param userID   当事人ID
     * @param followID 关注表的ID
     * @return
     */
    int selectByUserIDAndFollowID(Long userID, Long followID);
}
