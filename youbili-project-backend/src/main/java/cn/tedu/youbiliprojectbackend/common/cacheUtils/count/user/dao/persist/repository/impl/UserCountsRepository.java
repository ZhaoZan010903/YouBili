package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.dao.persist.repository.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.dao.persist.mapper.UserCountMapper;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.dao.persist.repository.IUserCountsRepository;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.pojo.vo.UserCountsCacheVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserCountsRepository implements IUserCountsRepository {
    @Autowired
    private UserCountMapper mapper;

    @Override
    public List<UserCountsCacheVO> selectAll() {
        return mapper.selectAll();
    }
}
