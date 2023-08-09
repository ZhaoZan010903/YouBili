package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.dao.persist.repository;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.pojo.vo.UserCountsCacheVO;

import java.util.List;

public interface IUserCountsRepository {
    List<UserCountsCacheVO> selectAll();
}
