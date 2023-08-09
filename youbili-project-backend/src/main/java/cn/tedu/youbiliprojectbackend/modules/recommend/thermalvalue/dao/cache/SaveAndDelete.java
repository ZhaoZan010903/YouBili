package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache;

import cn.tedu.youbiliprojectbackend.common.consts.VideoConsts;

public interface SaveAndDelete extends VideoConsts {
    /**
     * 保存7天热力值到Redis之中
     */
    void save();

    void delete();

}
