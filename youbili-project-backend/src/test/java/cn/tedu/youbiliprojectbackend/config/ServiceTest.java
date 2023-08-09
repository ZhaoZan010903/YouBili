package cn.tedu.youbiliprojectbackend.config;

import cn.tedu.youbiliprojectbackend.modules.tag.classification.service.ICategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {
    @Autowired
    ICategoryService iCategoryService;

    @Test
    void select() {
        System.out.println(iCategoryService.listTile());
    }
}
