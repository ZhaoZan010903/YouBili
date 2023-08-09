package cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.score;

import cn.tedu.youbiliprojectbackend.common.consts.VideoConsts;
import org.apache.mahout.cf.taste.common.TasteException;

import java.util.List;

/**
 * 协同过滤算法
 *
 * @author 赵錾
 */
public interface MahoutUserCF extends VideoConsts {
    List<Long> recommend(Long userID) throws TasteException;
}
