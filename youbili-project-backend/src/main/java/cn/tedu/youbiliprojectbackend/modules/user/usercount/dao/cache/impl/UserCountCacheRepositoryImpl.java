package cn.tedu.youbiliprojectbackend.modules.user.usercount.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.pojo.vo.UserCountsCacheVO;
import cn.tedu.youbiliprojectbackend.common.ex.ServiceException;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.user.usercount.dao.cache.UserCountCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

import static cn.tedu.youbiliprojectbackend.common.consts.CountConsts.USER_COUNT;

@Repository
public class UserCountCacheRepositoryImpl implements UserCountCacheRepository {

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 向redis之中的计数字段添加粉丝数+1;
     *
     * @param userID
     */
    @Override
    public void addFans(Long userID) {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        try {
            //当添加关注时,默认走到这里; 先认为这个字段的值未在redis之中修改过;
            UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + true);
            serializable.setFans(serializable.getFans() + 1);
            operations.set(USER_COUNT + userID + ":" + true, serializable);
            redisTemplate.rename(USER_COUNT + userID + ":" + true, USER_COUNT + userID + ":" + false);
        } catch (Throwable e) {
            // 在上面的try之中找不到key的值;则走到这个代码块之中
            try {
                //认为这个字段已经被修改过了; 则状态值字段为false
                UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + false);
                System.out.println(serializable);
                serializable.setFans(serializable.getFans() + 1);
                operations.set(USER_COUNT + userID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "用户ID错误");
            }
        }
    }

    /**
     * 向redis之中的计数字段粉丝数-1;
     *
     * @param userID
     */
    @Override
    public void removeFans(Long userID) {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        try {
            //当添加关注时,默认走到这里; 先认为这个字段的值未在redis之中修改过;
            UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + true);
            serializable.setFans(serializable.getFans() - 1);
            operations.set(USER_COUNT + userID + ":" + true, serializable);
            redisTemplate.rename(USER_COUNT + userID + ":" + true, USER_COUNT + userID + ":" + false);
        } catch (Throwable e) {
            // 在上面的try之中找不到key的值;则走到这个代码块之中
            try {
                //认为这个字段已经被修改过了; 则状态值字段为false
                UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + false);
                System.out.println(serializable);
                serializable.setFans(serializable.getFans() - 1);
                operations.set(USER_COUNT + userID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "用户ID错误");
            }
        }
    }

    /**
     * 向redis之中的计数字段关注数+1;
     *
     * @param userID
     */
    @Override
    public void addFollow(Long userID) {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        try {
            //当添加关注时,默认走到这里; 先认为这个字段的值未在redis之中修改过;
            UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + true);
            serializable.setFans(serializable.getFollows() + 1);
            operations.set(USER_COUNT + userID + ":" + true, serializable);
            redisTemplate.rename(USER_COUNT + userID + ":" + true, USER_COUNT + userID + ":" + false);
        } catch (Throwable e) {
            // 在上面的try之中找不到key的值;则走到这个代码块之中
            try {
                //认为这个字段已经被修改过了; 则状态值字段为false
                UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + false);
                System.out.println(serializable);
                serializable.setFans(serializable.getFollows() + 1);
                operations.set(USER_COUNT + userID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "用户ID错误");
            }
        }
    }

    /**
     * 向redis之中的计数字段粉丝数-1;
     *
     * @param userID
     */
    @Override
    public void removeFollow(Long userID) {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        try {
            //当添加关注时,默认走到这里; 先认为这个字段的值未在redis之中修改过;
            UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + true);
            serializable.setFans(serializable.getFollows() - 1);
            operations.set(USER_COUNT + userID + ":" + true, serializable);
            redisTemplate.rename(USER_COUNT + userID + ":" + true, USER_COUNT + userID + ":" + false);
        } catch (Throwable e) {
            // 在上面的try之中找不到key的值;则走到这个代码块之中
            try {
                //认为这个字段已经被修改过了; 则状态值字段为false
                UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + false);
                System.out.println(serializable);
                serializable.setFans(serializable.getFollows() - 1);
                operations.set(USER_COUNT + userID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "用户ID错误");
            }
        }
    }

    /**
     * 向redis之中的计数字段视频数+1;
     *
     * @param userID
     */
    @Override
    public void addVideoCount(Long userID) {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        try {
            //当添加关注时,默认走到这里; 先认为这个字段的值未在redis之中修改过;
            UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + true);
            serializable.setVideoCount(serializable.getVideoCount() + 1);
            operations.set(USER_COUNT + userID + ":" + true, serializable);
            redisTemplate.rename(USER_COUNT + userID + ":" + true, USER_COUNT + userID + ":" + false);
        } catch (Throwable e) {
            // 在上面的try之中找不到key的值;则走到这个代码块之中
            try {
                //认为这个字段已经被修改过了; 则状态值字段为false
                UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + false);
                System.out.println(serializable);
                serializable.setVideoCount(serializable.getVideoCount() + 1);
                operations.set(USER_COUNT + userID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "用户ID错误");
            }
        }
    }


    /**
     * 向redis之中的计数字段视频数-1;
     *
     * @param userID
     */
    @Override
    public void removeVideoCount(Long userID) {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        try {
            //当添加关注时,默认走到这里; 先认为这个字段的值未在redis之中修改过;
            UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + true);
            serializable.setVideoCount(serializable.getVideoCount() - 1);
            operations.set(USER_COUNT + userID + ":" + true, serializable);
            redisTemplate.rename(USER_COUNT + userID + ":" + true, USER_COUNT + userID + ":" + false);
        } catch (Throwable e) {
            // 在上面的try之中找不到key的值;则走到这个代码块之中
            try {
                //认为这个字段已经被修改过了; 则状态值字段为false
                UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + false);
                System.out.println(serializable);
                serializable.setVideoCount(serializable.getVideoCount() - 1);
                operations.set(USER_COUNT + userID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "用户ID错误");
            }
        }
    }

    /**
     * 向redis之中的计数字段总点赞数-1;
     *
     * @param userID
     */
    @Override
    public void addTotalCount(Long userID) {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        try {
            //当添加关注时,默认走到这里; 先认为这个字段的值未在redis之中修改过;
            UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + true);
            serializable.setTotalLikes(serializable.getTotalLikes() + 1);
            operations.set(USER_COUNT + userID + ":" + true, serializable);
            redisTemplate.rename(USER_COUNT + userID + ":" + true, USER_COUNT + userID + ":" + false);
        } catch (Throwable e) {
            // 在上面的try之中找不到key的值;则走到这个代码块之中
            try {
                //认为这个字段已经被修改过了; 则状态值字段为false
                UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + false);
                System.out.println(serializable);
                serializable.setTotalLikes(serializable.getTotalLikes() + 1);
                operations.set(USER_COUNT + userID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "用户ID错误");
            }
        }
    }

    /**
     * 向redis之中的计数字段总点赞数-1;
     *
     * @param userID
     */
    @Override
    public void removeTotalCount(Long userID) {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        try {
            //当添加关注时,默认走到这里; 先认为这个字段的值未在redis之中修改过;
            UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + true);
            serializable.setTotalLikes(serializable.getTotalLikes() - 1);
            operations.set(USER_COUNT + userID + ":" + true, serializable);
            redisTemplate.rename(USER_COUNT + userID + ":" + true, USER_COUNT + userID + ":" + false);
        } catch (Throwable e) {
            // 在上面的try之中找不到key的值;则走到这个代码块之中
            try {
                //认为这个字段已经被修改过了; 则状态值字段为false
                UserCountsCacheVO serializable = (UserCountsCacheVO) operations.get(USER_COUNT + userID + ":" + false);
                System.out.println(serializable);
                serializable.setTotalLikes(serializable.getTotalLikes() - 1);
                operations.set(USER_COUNT + userID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "用户ID错误");
            }
        }
    }


}
