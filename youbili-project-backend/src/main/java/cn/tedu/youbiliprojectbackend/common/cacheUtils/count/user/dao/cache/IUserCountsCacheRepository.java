package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.dao.cache;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.pojo.vo.UserCountsCacheVO;

import java.util.List;

public interface IUserCountsCacheRepository {
    void save(List<UserCountsCacheVO> userCounts);

    void deleteAll();
}
