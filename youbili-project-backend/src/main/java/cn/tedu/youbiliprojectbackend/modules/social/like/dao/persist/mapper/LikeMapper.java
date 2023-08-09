package cn.tedu.youbiliprojectbackend.modules.social.like.dao.persist.mapper;

import cn.tedu.youbiliprojectbackend.modules.social.like.pojo.entity.Like;
import cn.tedu.youbiliprojectbackend.modules.social.like.pojo.vo.LikeTypeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeMapper extends BaseMapper<Like> {
    /**
     * 插入点赞和点踩数据
     *
     * @param likes
     * @return
     */
    int insert(Like likes);

    /**
     * 取消点赞
     *
     * @return
     */
    int delete(Long userID, Long videoID);

    /**
     * 查询是否点赞
     */
    LikeTypeVO selectTypeEnableByVideoID(Long userID, Long videoID);

}
