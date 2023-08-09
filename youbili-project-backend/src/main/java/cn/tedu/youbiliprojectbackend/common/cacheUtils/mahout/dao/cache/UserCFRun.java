package cn.tedu.youbiliprojectbackend.common.cacheUtils.mahout.dao.cache;

import cn.tedu.youbiliprojectbackend.common.consts.MahoutConsts;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.pojo.UserArticleOperation;

import java.util.List;

public interface UserCFRun extends MahoutConsts {
    void select(List<UserArticleOperation> list);

    void delete();
}
