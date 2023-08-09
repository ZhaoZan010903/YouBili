package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.score;


import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.HeatVideo;

import java.util.List;

/**
 * 热力值公式计算类
 */
public interface ThermalValue {
    /**
     * 1.查询数据获得信息
     * 2.通过计算每个视频的 点赞数,收藏数,观看数,弹幕数,评论数 ; 点踩数 ; 以及每个视频上传时间的衰减因子来获取每个视频的热力值;
     * 3.将计算好的视频按照数据再排列;
     * 4.用于热门推荐以及基于物品的推荐之中;
     */


    /**
     * 计算近7天的视频热力值
     *
     * @return
     */
    List<HeatVideo> heatValue();
}
