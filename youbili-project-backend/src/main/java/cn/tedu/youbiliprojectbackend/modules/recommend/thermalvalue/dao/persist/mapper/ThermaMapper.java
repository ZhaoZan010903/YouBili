package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.persist.mapper;

import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.videoDay;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.videoSum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThermaMapper extends BaseMapper<videoSum> {
    List<videoDay> selectByDay();
}
