package cn.tedu.youbiliprojectbackend.common.data.dao.persist.repository.impl;

import cn.tedu.youbiliprojectbackend.common.data.dao.persist.repository.DataRepository;
import cn.tedu.youbiliprojectbackend.common.data.mapper.DataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author 赵錾
 */
@Repository
@Slf4j
public class DataRepositoryImpl implements DataRepository {

    @Autowired
    DataMapper dataMapper;

    /**
     * 将收藏表,喜欢表,历史表的数据
     * 定时洗入到mahout表之中,
     * 为后续的推荐算法做数据支撑
     */
    @Override
    public void MahoutData() {
        log.debug("开始向数据库之中清洗数据");
        dataMapper.FavoriteMahoutMapper();
        dataMapper.LikesMahoutMapper();
        dataMapper.HistoryMahoutMapper();
    }
}
