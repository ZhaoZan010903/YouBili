package cn.tedu.youbiliprojectbackend;

import cn.tedu.youbiliprojectbackend.modules.user.favorite.dao.persist.mapper.FavoriteMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class favoriteTest {

    @Autowired
    FavoriteMapper favoriteMapper;

    @Test
    void list() {

        System.out.println(favoriteMapper.selectListById(3L));
    }
}
