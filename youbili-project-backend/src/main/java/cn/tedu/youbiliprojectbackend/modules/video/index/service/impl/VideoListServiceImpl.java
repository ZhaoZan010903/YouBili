package cn.tedu.youbiliprojectbackend.modules.video.index.service.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.pojo.vo.UserCountsCacheVO;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.pojo.VideoCount;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.modules.video.index.dao.cache.IVideoListCache;
import cn.tedu.youbiliprojectbackend.modules.video.index.dao.persist.mapper.TagMapper;
import cn.tedu.youbiliprojectbackend.modules.video.index.pojo.vo.VideoTagVO;
import cn.tedu.youbiliprojectbackend.modules.video.index.pojo.vo.videoListVO;
import cn.tedu.youbiliprojectbackend.modules.video.index.service.IVideoListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VideoListServiceImpl implements IVideoListService {


    @Autowired
    IVideoListCache videoListCache;
    @Autowired
    TagMapper tagMapper;

    @Override
    public videoListVO selectAll(Long videoID) {
        log.debug("开始查找相关视频的ID等");
        videoListVO videoListVO = new videoListVO();
        log.debug("videoID : {}", videoID);
        VideoUserCache vuc = videoListCache.videoListselect(videoID);
        log.debug("{}", vuc);
        UserCountsCacheVO ucc = videoListCache.listFansFolCount(vuc.getUserID());
        VideoCount vc = videoListCache.listLike(videoID);

        //vuc
        videoListVO.setVideoID(vuc.getVideoID())
                .setUserID(vuc.getUserID())
                .setTitle(vuc.getTitle())
                .setDuration(vuc.getDuration())
                .setUploadDate(vuc.getUploadDate())
                .setVideoSrcUrl(vuc.getVideoSrcUrl())
                .setVideoImgUrl(vuc.getVideoImgUrl())
                .setNickname(vuc.getNickname())
                .setImgUrl(vuc.getImgUrl())
                .setDescription(vuc.getDescription());

        //ucc
        videoListVO.setFans(ucc.getFans()).setFollow(ucc.getFollows());

        //vc
        videoListVO.setLikeCount(vc.getLikeCount())
                .setViewCount(vc.getViewCount())
                .setFavoriteCount(vc.getFavoriteCount())
                .setDislikeCount(vc.getDislikeCount())
                .setBarrageCount(vc.getBarrageCount())
                .setCommentCount(vc.getCommentCount());


        return videoListVO;
    }

    @Override
    public List<VideoTagVO> selectTag(Long videoID) {
        return tagMapper.select(videoID);
    }
}
