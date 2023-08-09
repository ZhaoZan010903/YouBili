package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.service;

import org.springframework.stereotype.Service;

@Service
public interface IUserCountsService {
    void saveCacheUserCounts();
}
