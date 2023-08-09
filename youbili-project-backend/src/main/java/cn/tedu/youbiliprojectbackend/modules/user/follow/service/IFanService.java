package cn.tedu.youbiliprojectbackend.modules.user.follow.service;

import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo.FanListVO;

import java.util.List;

public interface IFanService {
    /**
     * 用户关注列表
     *
     * @param userID 当事人ID
     * @return 返回用户粉丝列表
     */
    List<FanListVO> list(Long userID);

    /**
     * 删除粉丝
     *
     * @param userID   当事人ID
     * @param followID 关注表的ID
     */
    void delete(Long userID, Long followID);
}
