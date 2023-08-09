package cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.mapper;

import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.pojo.UserArticleOperation;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 赵錾
 */
@Repository
public interface MahoutMapper {
    //    List<MahoutData> select();
    List<UserArticleOperation> selectuser();
}
