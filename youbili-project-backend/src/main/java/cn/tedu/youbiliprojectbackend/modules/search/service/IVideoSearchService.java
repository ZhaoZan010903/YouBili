package cn.tedu.youbiliprojectbackend.modules.search.service;

import cn.tedu.youbiliprojectbackend.modules.search.pojo.param.VideoSearchParam;
import cn.tedu.youbiliprojectbackend.modules.search.pojo.vo.VideoSearchVO;

import java.util.List;

public interface IVideoSearchService {
    /**
     * 重建搜索数据（重新从数据库中获取数据并写入到ES中）
     */
    void rebuildSearch();

    List<VideoSearchVO> searchByTitle(String title);

    void insertIntoEs(VideoSearchParam videoSearchParam);

    void update(VideoSearchParam videoSearchParam);

    void deleteByUserIDAndVideoID(Long userID, Long videoID);
}
