package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.service.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache.SaveAndDelete;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache.Select;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.service.ThermalValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ThermalValueServiceImpl implements ThermalValueService {

    @Autowired
    SaveAndDelete saveAndDelete;
    @Autowired
    Select select;


    /**
     * 定时更新热度排行榜
     */
    @Override
    public void Runner() {
        saveAndDelete.delete();
        saveAndDelete.save();
    }

    /**
     * 根据热度值ID查找视频列表
     */
    @Override
    public List<VideoUserCache> select() {
        List<VideoUserCache> selecta = select.Select();
        return selecta;
    }

    @Override
    public List<VideoUserCache> select1() {
        List<VideoUserCache> select1 = select.Select();
        int i = select1.size() - 1; //从最后一个索引开始
        log.debug("{}", select1.size());
        while (select1.size() > 4) {
            select1.remove(i); //删除最后一个元素
            i--; //索引递减
        }
        return select1;
    }

}
