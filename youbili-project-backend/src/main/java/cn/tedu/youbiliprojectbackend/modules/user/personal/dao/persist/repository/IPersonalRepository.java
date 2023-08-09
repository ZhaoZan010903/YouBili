package cn.tedu.youbiliprojectbackend.modules.user.personal.dao.persist.repository;

import cn.tedu.youbiliprojectbackend.modules.user.personal.pojo.vo.CenterVideoInfoVO;
import cn.tedu.youbiliprojectbackend.modules.user.personal.pojo.vo.PersonalCenterUserInfoVO;

import java.util.List;

public interface IPersonalRepository {
    List<CenterVideoInfoVO> selectVideoInfoByUserId(Long id);

    PersonalCenterUserInfoVO selectUserInfoByUserId(Long id);

    List<CenterVideoInfoVO> selectFavoriteInfoByUserId(Long id);

    List<CenterVideoInfoVO> selectHistoryInfoByUserId(Long id);
}
