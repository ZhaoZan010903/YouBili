package cn.tedu.youbiliprojectbackend.modules.user.personal.dao.persist.repository.impl;

import cn.tedu.youbiliprojectbackend.modules.user.personal.dao.persist.mapper.PersonalMapper;
import cn.tedu.youbiliprojectbackend.modules.user.personal.dao.persist.repository.IPersonalRepository;
import cn.tedu.youbiliprojectbackend.modules.user.personal.pojo.vo.CenterVideoInfoVO;
import cn.tedu.youbiliprojectbackend.modules.user.personal.pojo.vo.PersonalCenterUserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonalRepository implements IPersonalRepository {

    @Autowired
    PersonalMapper mapper;


    @Override
    public List<CenterVideoInfoVO> selectVideoInfoByUserId(Long id) {
        return mapper.selectVideoInfoByUserId(id);
    }

    @Override
    public PersonalCenterUserInfoVO selectUserInfoByUserId(Long id) {
        return mapper.selectUserInfoByUserId(id);
    }

    @Override
    public List<CenterVideoInfoVO> selectFavoriteInfoByUserId(Long id) {
        return mapper.selectFavoriteInfoByUserId(id);
    }

    @Override
    public List<CenterVideoInfoVO> selectHistoryInfoByUserId(Long id) {
        return mapper.selectHistoryInfoByUserId(id);
    }
}
