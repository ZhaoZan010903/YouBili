package cn.tedu.youbiliprojectbackend.common.data.dao.persist.repository;

/**
 * 数据清洗类
 */
public interface DataRepository {

    /**
     * 将收藏表,喜欢表,历史表的数据
     * 定时洗入到mahout表之中,
     * 为后续的推荐算法做数据支撑
     */
    void MahoutData();
}
