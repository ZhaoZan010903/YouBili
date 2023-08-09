package cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.score.impl;

import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.score.ListVideoCF;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.score.MahoutUserCF;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 通过协同过滤算法算出视频的ID
 * 如果用户的数据量在样本模型之中数量太少则将转换使用random达成随机推荐
 *
 * @author 赵錾
 */
@Repository
@Slf4j
public class ListVideoCFImpl implements ListVideoCF {

    @Autowired
    MahoutUserCF mahoutUserCF;


    /**
     * @param userID 用户ID
     * @return 返回视频ID列表
     */
    @Override
    public List<Long> videoIDList(Long userID) {
        List<Long> userCF = null;
        try {
            userCF = mahoutUserCF.recommend(userID);
            log.debug("开始使用协同过滤算法基于用户推荐视频");
        } catch (TasteException e) {
            log.debug("没有足够的模型数据;开始走随机方法");
            Set<Long> set = new HashSet<>();
            Random ra = new Random();
            while (userCF.size() <= 12) {
                Long i = (long) ra.nextInt(30) + 1;
                if (set.add(i)) {
                    userCF.add(i);
                }
            }

        }


        return userCF;
    }
}
