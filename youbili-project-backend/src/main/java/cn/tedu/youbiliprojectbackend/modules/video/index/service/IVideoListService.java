package cn.tedu.youbiliprojectbackend.modules.video.index.service;

import cn.tedu.youbiliprojectbackend.modules.video.index.pojo.vo.VideoTagVO;
import cn.tedu.youbiliprojectbackend.modules.video.index.pojo.vo.videoListVO;

import java.util.List;

public interface IVideoListService {
    videoListVO selectAll(Long videoID);

    List<VideoTagVO> selectTag(Long videoID);
}
