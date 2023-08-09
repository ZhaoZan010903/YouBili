package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.persist.repository.impl;

import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.persist.mapper.ThermaMapper;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.persist.repository.ThremaRepository;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.videoDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ThremaRepositoryImpl implements ThremaRepository {

    @Autowired
    ThermaMapper thermaMapper;

    @Override
    public List<videoDay> video() {
        return thermaMapper.selectByDay();
    }
}
