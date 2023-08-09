package cn.tedu.youbiliprojectbackend.config;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.pojo.vo.UserCountsCacheVO;
import cn.tedu.youbiliprojectbackend.common.consts.CountConsts;
import cn.tedu.youbiliprojectbackend.common.ex.ServiceException;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.video.index.dao.persist.mapper.TagMapper;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.Serializable;

@SpringBootTest
public class CountDemo implements CountConsts {

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    TagMapper tagMapper;

    /**
     * 添加粉丝案例
     */
    @Test
    void demo() {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        /**
         * 思路:
         * redis 之中 key 的储值拼接为 User:Count:userID:true OR false (为状态值)
         * 其中,userID 和 true OR false 为变量值
         * 当对某一个用户进行计数字段的改变时,key之中的状态值可以能为true,也可能为false
         * 为了减少对于 "*" 模糊查询的使用. 使用try-catch;
         * 1.try之中代码为当状态码为true时, 通过key取出value,再将要修改的value的值修改;再修改key的true为false
         * 2.1当try之中的key找不到value时,则报错.代码走到catch块之中的try的内容为状态码为false;取出value且修改;
         * 2.2当catch块之中的try还报错的时候 则抛出错误 为用户ID错误
         */

        try {
            //当添加关注时,默认走到这里; 先认为这个字段的值未在redis之中修改过;
            UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + "1" + ":" + true);
            serializable.setFans(serializable.getFans() + 1);
            operations.set(USER_COUNT + "1" + ":" + true, serializable);
            redisTemplate.rename(USER_COUNT + "1" + ":" + true, USER_COUNT + "1" + ":" + false);
        } catch (Throwable e) {
            // 在上面的try之中找不到key的值;则走到这个代码块之中
            try {
                //认为这个字段已经被修改过了; 则状态值字段为false
                UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + "1" + ":" + false);
                System.out.println(serializable);
                serializable.setFans(serializable.getFans() + 1);
                operations.set(USER_COUNT + "1" + ":" + false, serializable);
            } catch (Throwable e1) {
                //上面的try还是报错则走到这里
                //当 userID 不存在的时候则走到这里
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "用户ID错误");
            }
        } finally {
            //最终会走到这里!
            //执行下面的代码块 如返回值等;
            System.out.println(1111);
        }


    }


    @Test
    void tag() {
        tagMapper.select(38L);
    }
}
