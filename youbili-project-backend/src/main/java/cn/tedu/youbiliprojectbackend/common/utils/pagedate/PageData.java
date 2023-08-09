package cn.tedu.youbiliprojectbackend.common.utils.pagedate;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据类
 *
 * @author java@tedu.cn
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class PageData<T> implements Serializable {

    /**
     * 每页记录数
     */
    private Integer pageSize;
    /**
     * 记录总数
     */
    private Long total;
    /**
     * 当前页码
     */
    private Integer currentPage;
    /**
     * 最大页码
     */
    private Integer maxPage;
    /**
     * 数据列表
     */
    private List<T> list;

}