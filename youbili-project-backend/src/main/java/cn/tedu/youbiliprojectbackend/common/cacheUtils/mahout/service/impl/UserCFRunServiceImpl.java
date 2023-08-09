package cn.tedu.youbiliprojectbackend.common.cacheUtils.mahout.service.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.mahout.dao.cache.UserCFRun;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.mahout.service.UserCFRunService;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.mapper.MahoutMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserCFRunServiceImpl implements UserCFRunService {
    @Autowired
    UserCFRun userCFRun;
    @Autowired
    MahoutMapper mahoutMapper;


    @Override
    public void UserCFRun() {
        log.debug("开始向缓存中写入数据");
        userCFRun.delete();
        userCFRun.select(mahoutMapper.selectuser());
    }
}
