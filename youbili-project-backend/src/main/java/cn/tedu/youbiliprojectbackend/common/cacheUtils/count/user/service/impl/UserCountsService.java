package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.service.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.dao.cache.IUserCountsCacheRepository;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.dao.persist.repository.IUserCountsRepository;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.service.IUserCountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCountsService implements IUserCountsService {
    @Autowired
    IUserCountsCacheRepository cacheRepository;
    @Autowired
    private IUserCountsRepository repository;

    @Override
    public void saveCacheUserCounts() {
        cacheRepository.deleteAll();
        cacheRepository.save(repository.selectAll());
    }
}
