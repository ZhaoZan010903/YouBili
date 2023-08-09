package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.score.impl;

import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache.heatCache;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.HeatVideo;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.videoDTO;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.videoSum;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.score.ThermalValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class ThermaValueImpl implements ThermalValue {

    @Autowired
    heatCache heatCache;


    /**
     * 具体实现:
     * 将redis之中的计数信息取出计算
     *
     * @return
     */
    @Override
    public List<HeatVideo> heatValue() {

        List<videoDTO> videoDTOS = heatCache.listCount();
        List<videoSum> videoSumList = new ArrayList<>();
        for (videoDTO videoDTO : videoDTOS) {
            double heat = videoDTO.getCommentCount() * 0.4 + videoDTO.getLikeCount() * 0.3 - videoDTO.getDislikeCount() * 0.4 + videoDTO.getFavoriteCount() * 0.5 + videoDTO.getBarrageCount() * 0.6 + videoDTO.getViewCount() * 0.1;
            videoSum videoSum = new videoSum().setVideoID(videoDTO.getVideoID()).setHeatValue(heat).setUploadDate(videoDTO.getUploadDate());
            videoSumList.add(videoSum);
        }
        List<HeatVideo> heatVideos = new ArrayList<>();
        for (videoSum videoSum : videoSumList) {
            Duration duration = Duration.between(videoSum.getUploadDate(), LocalDateTime.now());
            long hours = duration.toHours() / 100000;
            double result = Math.pow(Math.E, hours);
            Double heatValue = videoSum.getHeatValue();
            Double heatData = result * heatValue;
            HeatVideo heatVideo = new HeatVideo();
            heatVideo.setVideoID(videoSum.getVideoID());
            heatVideo.setHeatDay(heatData);
            heatVideos.add(heatVideo);
        }

        return heatVideos;
    }
}
