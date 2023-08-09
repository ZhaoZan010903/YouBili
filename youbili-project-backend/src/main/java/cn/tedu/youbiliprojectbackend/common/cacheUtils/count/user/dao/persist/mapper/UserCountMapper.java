package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.dao.persist.mapper;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.pojo.vo.UserCountsCacheVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCountMapper {
    List<UserCountsCacheVO> selectAll();
}
