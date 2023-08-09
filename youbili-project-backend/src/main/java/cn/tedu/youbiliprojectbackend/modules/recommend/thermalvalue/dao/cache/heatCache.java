package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.common.consts.CountConsts;
import cn.tedu.youbiliprojectbackend.common.consts.VideoConsts;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.videoDTO;

import java.util.List;

public interface heatCache extends CountConsts, VideoConsts {

    /**
     * 将Redis之中所有的计数字段取出并计算
     *
     * @return
     */
    List<videoDTO> listCount();

    /**
     * 查询热力值排行榜 并返回对应数据
     *
     * @return
     */
    List<VideoUserCache> select();
}
