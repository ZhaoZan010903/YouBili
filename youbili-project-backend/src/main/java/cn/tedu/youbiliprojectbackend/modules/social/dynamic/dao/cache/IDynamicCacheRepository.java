package cn.tedu.youbiliprojectbackend.modules.social.dynamic.dao.cache;


import cn.tedu.youbiliprojectbackend.common.utils.pagedate.PageData;
import cn.tedu.youbiliprojectbackend.modules.social.dynamic.pojo.vo.DynamicListVO;

import java.util.List;

/**
 * 处理类别数据的缓存的存储库接口
 *
 * @author java@tedu.cn
 * @version 1.0
 */
public interface IDynamicCacheRepository {


    /**
     * 从缓存中读取动态列表
     *
     * @param userID 用户id
     * @return 类别列表
     */
    List<DynamicListVO> list(Long userID);

    /**
     * 查询动态列表
     *
     * @param userID   用户id
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 动态列表
     */
    PageData<DynamicListVO> list(Long userID, Integer pageNum, Integer pageSize);

}