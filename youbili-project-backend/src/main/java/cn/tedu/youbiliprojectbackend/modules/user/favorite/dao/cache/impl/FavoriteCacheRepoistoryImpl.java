package cn.tedu.youbiliprojectbackend.modules.user.favorite.dao.cache.impl;


import cn.tedu.youbiliprojectbackend.common.consts.CountConsts;
import cn.tedu.youbiliprojectbackend.modules.user.favorite.dao.cache.FavoriteCacheRepoistory;
import cn.tedu.youbiliprojectbackend.modules.user.favorite.dao.persist.mapper.FavoriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FavoriteCacheRepoistoryImpl implements FavoriteCacheRepoistory, CountConsts {

    @Autowired
    private FavoriteMapper favoriteMapper;


    @Override
    public void save() {
    }


}
