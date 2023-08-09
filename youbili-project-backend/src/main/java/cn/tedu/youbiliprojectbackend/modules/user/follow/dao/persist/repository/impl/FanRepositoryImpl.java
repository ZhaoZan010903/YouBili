package cn.tedu.youbiliprojectbackend.modules.user.follow.dao.persist.repository.impl;

import cn.tedu.youbiliprojectbackend.modules.user.follow.dao.persist.mapper.FanMapper;
import cn.tedu.youbiliprojectbackend.modules.user.follow.dao.persist.repository.IFanRepository;
import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo.FanListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class FanRepositoryImpl implements IFanRepository {
    @Autowired
    FanMapper fanMapper;

    /**
     * 用户关注列表
     *
     * @param userID 当事人ID
     * @return 返回用户粉丝列表
     */
    @Override
    public List<FanListVO> selectListByUserId(Long userID) {
        return fanMapper.selectListByUserId(userID);
    }

    /**
     * 删除粉丝
     *
     * @param userID   当事人ID
     * @param followID
     * @return
     */
    @Override
    public int delete(Long userID, Long followID) {
        return fanMapper.delete(userID, followID);
    }

    @Override
    public int selectByUserIDAndFollowID(Long userID, Long followID) {
        return fanMapper.selectByUserIDAndFollowID(userID, followID);
    }
}
