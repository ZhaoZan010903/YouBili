package cn.tedu.youbiliprojectbackend.common.data.dao.persist.cache.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.mahout.service.UserCFRunService;
import cn.tedu.youbiliprojectbackend.common.data.dao.persist.cache.DataCacheUserCF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 将更新后的字段写入缓存
 */
@Repository
@Slf4j
public class DataCacheUserCFImpl implements DataCacheUserCF {
    @Autowired
    UserCFRunService userCFRun;

    /**
     * 写入更新后的字段 定时更新
     */
    @Override
    public void selectData() {
        log.debug("开始向缓存之中写入MahoutData");
        userCFRun.UserCFRun();
    }
}
