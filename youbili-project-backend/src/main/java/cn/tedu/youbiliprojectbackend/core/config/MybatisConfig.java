package cn.tedu.youbiliprojectbackend.core.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(
        {
                "cn.tedu.youbiliprojectbackend.modules.user.register.dao.mapper",
                "cn.tedu.youbiliprojectbackend.modules.user.favorite.dao.persist.mapper",
                "cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.persist.mapper",
                "cn.tedu.youbiliprojectbackend.modules.user.follow.dao.persist.mapper",
                "cn.tedu.youbiliprojectbackend.modules.user.fan.dao.persist.mapper",
                "cn.tedu.youbiliprojectbackend.modules.user.personal.dao.persist.mapper",
                "cn.tedu.youbiliprojectbackend.modules.orders.dao.mapper",
                "cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.dao.persist.mapper",
                "cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.dao.persist.mapper",
                "cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.dao.persist.mapper",
                "cn.tedu.youbiliprojectbackend.modules.tag.classification.dao.mapper",
                "cn.tedu.youbiliprojectbackend.modules.social.notice.dao.persist.mapper",
                "cn.tedu.youbiliprojectbackend.modules.video.uplaod.dao.mapper",
                "cn.tedu.youbiliprojectbackend.modules.social.dynamic.dao.persist.mapper.DynamicMapper",
                "cn.tedu.youbiliprojectbackend.modules.user.history.dao.mapper",
                "cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.mapper",
                "cn.tedu.youbiliprojectbackend.common.data.mapper",
                "cn.tedu.youbiliprojectbackend.modules.user.account.dao.mapper",
                "cn.tedu.youbiliprojectbackend.modules.social.like.dao.persist.mapper",
                "cn.tedu.youbiliprojectbackend.modules.social.comment.dao.persist.mapper",
                "cn.tedu.youbiliprojectbackend.modules.video.index.dao.persist.mapper"
        })
public class MybatisConfig {
}
