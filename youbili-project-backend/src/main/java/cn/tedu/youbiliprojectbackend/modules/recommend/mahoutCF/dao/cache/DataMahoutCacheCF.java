package cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.dao.cache;

import cn.tedu.youbiliprojectbackend.common.consts.MahoutConsts;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.pojo.UserArticleOperation;

import java.util.List;

public interface DataMahoutCacheCF extends MahoutConsts {

    /**
     * 查找数据
     *
     * @return 返回数据集合
     */
    List<UserArticleOperation> selectData();
}
