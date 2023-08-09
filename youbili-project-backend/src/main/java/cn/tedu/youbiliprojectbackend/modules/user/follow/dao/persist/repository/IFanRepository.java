package cn.tedu.youbiliprojectbackend.modules.user.follow.dao.persist.repository;

import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo.FanListVO;

import java.util.List;

public interface IFanRepository {
    /**
     * 查询用户关注列表
     *
     * @param userID 当事人ID
     * @return 返回用户粉丝列表
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
     * 查询用户时候有该粉丝
     *
     * @param userID   当事人ID
     * @param followID 关注表的ID
     * @return
     */
    int selectByUserIDAndFollowID(Long userID, Long followID);
}
