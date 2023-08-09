package cn.tedu.youbiliprojectbackend.modules.social.dynamic.service;

import cn.tedu.youbiliprojectbackend.modules.social.dynamic.pojo.vo.DynamicListVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 处理动态数据的业务接口
 *
 * @author java@tedu.cn
 * @version 1.0
 */
@Transactional
public interface IDynameicService {

    /**
     * 查询动态列表，将使用默认的每页记录数
     *
     * @return 查询标签列表
     */
    List<DynamicListVO> list(Long userId);
//
//    /**
//     * 查询动态列表，将使用默认的每页记录数
//     *
//     * @param pageNum 页码
//     * @return 查询标签列表
//     */
//    PageData<DynamicListVO> list(Long userId,Integer pageNum);
//
//    /**
//     * 查询动态列表
//     *
//     * @param pageNum  页码
//     * @param pageSize 每页记录数
//     * @return 标签列表
//     */
//    PageData<DynamicListVO> list(Long userId,Integer pageNum, Integer pageSize);

}
