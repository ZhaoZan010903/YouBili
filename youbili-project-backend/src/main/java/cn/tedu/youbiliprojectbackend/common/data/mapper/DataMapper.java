package cn.tedu.youbiliprojectbackend.common.data.mapper;

import org.springframework.stereotype.Repository;

/**
 * 数据清洗类;
 *
 * @author 赵錾
 */
@Repository
public interface DataMapper {
    void HistoryMahoutMapper();

    void LikesMahoutMapper();

    void FavoriteMahoutMapper();

    void VideoDateMapper();
}
