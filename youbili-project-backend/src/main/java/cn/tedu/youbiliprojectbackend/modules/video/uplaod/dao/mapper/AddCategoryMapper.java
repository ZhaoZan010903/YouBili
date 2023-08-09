package cn.tedu.youbiliprojectbackend.modules.video.uplaod.dao.mapper;

import cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto.VideoCategoryDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface AddCategoryMapper {
    int insertCategory(VideoCategoryDTO videoCategoryDTO);
}
