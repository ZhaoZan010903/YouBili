package cn.tedu.youbiliprojectbackend.core.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Validation配置类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Slf4j
@Configuration
public class ValidationConfiguration {

    public ValidationConfiguration() {
        log.debug("创建配置类对象：ValidationConfiguration");
    }

    @Bean
    public Validator validator() {
        return Validation.byProvider(HibernateValidator.class)
                .configure() // 开始配置
                .failFast(true) // 配置快速失败
                .buildValidatorFactory() // 构建Validator工厂
                .getValidator(); // 从Validator工厂中获取Validator对象
    }

}
