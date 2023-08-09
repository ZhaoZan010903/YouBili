package cn.tedu.youbiliprojectbackend.core.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 基于MyBatis Plus的自动填充时间的处理器类
 *
 * @author java@tedu.cn
 * @version 1.0
 */
@Slf4j
@Component
public class TimeMetaObjectHandler implements MetaObjectHandler {

    /**
     * 数据创建时间的属性名
     */
    public static final String FIELD_CREATE_TIME = "gmtCreate";
    /**
     * 数据最后修改时间的属性名
     */
    public static final String FIELD_UPDATE_TIME = "gmtModified";

    public TimeMetaObjectHandler() {
        log.info("创建MyBatis Plus的自动填充数据的处理器对象：TimeMetaObjectHandler");
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName(FIELD_CREATE_TIME, now, metaObject);
        this.setFieldValByName(FIELD_UPDATE_TIME, now, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName(FIELD_UPDATE_TIME, now, metaObject);
    }

}