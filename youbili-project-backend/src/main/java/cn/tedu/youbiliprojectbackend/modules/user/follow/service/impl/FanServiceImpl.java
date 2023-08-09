package cn.tedu.youbiliprojectbackend.modules.user.follow.service.impl;

import cn.tedu.youbiliprojectbackend.common.ex.ServiceException;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.user.follow.dao.persist.repository.IFanRepository;
import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo.FanListVO;
import cn.tedu.youbiliprojectbackend.modules.user.follow.service.IFanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FanServiceImpl implements IFanService {
    @Autowired
    IFanRepository fanRepository;

    @Override
    /**
     * 用户关注列表
     * @param userID 当事人ID
     * @return 返回用户粉丝列表
     */
    public List<FanListVO> list(Long userID) {
        List<FanListVO> list = fanRepository.selectListByUserId(userID);

//        if(list == null) {
//            throw new ServiceException(ServiceCode.ERROR_CONFLICT,"没有查询到响应结果");
//        }

        return list;
    }

    @Override
    /**
     * 删除粉丝
     * @param userID 当事人ID
     * @param followID 关注表的ID
     */
    public void delete(Long userID, Long followID) {
        int row = fanRepository.selectByUserIDAndFollowID(userID, followID);
        if (row < 1) {
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, "您没有该粉丝!");
        }
        fanRepository.delete(userID, followID);

    }
}
