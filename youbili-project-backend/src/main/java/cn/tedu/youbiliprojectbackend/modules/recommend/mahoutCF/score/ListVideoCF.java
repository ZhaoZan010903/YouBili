package cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.score;

import java.util.List;

/**
 * 封装推荐方法
 *
 * @author 赵錾
 */
public interface ListVideoCF {
    /**
     * @param userID 用户ID
     * @return 返回视频ID列表
     */
    List<Long> videoIDList(Long userID);
}
