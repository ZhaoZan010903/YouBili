package cn.tedu.youbiliprojectbackend.modules.social.dynamic.service.impl;

import cn.tedu.youbiliprojectbackend.modules.social.dynamic.dao.cache.IDynamicCacheRepository;
import cn.tedu.youbiliprojectbackend.modules.social.dynamic.pojo.vo.DynamicListVO;
import cn.tedu.youbiliprojectbackend.modules.social.dynamic.service.IDynameicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author java@tedu.cn
 * @version 1.0
 */
@Slf4j
@Service
public class DynameicServiceImpl implements IDynameicService {

    @Autowired
    private IDynamicCacheRepository dynamicCacheRepository;


    public DynameicServiceImpl() {
        log.info("创建业务对象：DynameicServiceImpl");
    }

    @Override
    public List<DynamicListVO> list(Long userId) {
        log.debug("开始处理【查询动态列表】的业务,参数{}", userId);
        return dynamicCacheRepository.list(userId);
    }
}
