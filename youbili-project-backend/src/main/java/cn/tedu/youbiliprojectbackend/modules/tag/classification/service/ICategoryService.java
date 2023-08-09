package cn.tedu.youbiliprojectbackend.modules.tag.classification.service;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryTitleVO;

import java.util.List;

/**
 * 类别的service接口
 *
 * @author 万佳兴
 */
public interface ICategoryService {

    /**
     * 查询视频分类名称
     *
     * @return CategoryTitleVO
     */
    List<CategoryTitleVO> listTile();

    /**
     * 根据分类ID查询出视频相关信息
     */
    List<VideoUserCache> selectVideo(Long categoryID);
}
