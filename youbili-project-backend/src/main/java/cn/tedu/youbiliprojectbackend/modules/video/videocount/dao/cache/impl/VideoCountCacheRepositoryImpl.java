package cn.tedu.youbiliprojectbackend.modules.video.videocount.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.pojo.VideoCount;
import cn.tedu.youbiliprojectbackend.common.ex.ServiceException;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.video.videocount.dao.cache.VideoCountCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

import static cn.tedu.youbiliprojectbackend.common.consts.CountConsts.VIDEO_COUNT;


@Repository
public class VideoCountCacheRepositoryImpl implements VideoCountCacheRepository {

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 向Redis之中的计数字段添加观看数+1
     *
     * @param videoID
     */
    @Override
    public void addView(Long videoID) {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        try {
            //当点击观看视频时默认走到这里
            VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + true);
            serializable.setViewCount(serializable.getViewCount() + 1);
            opsForValue.set(VIDEO_COUNT + videoID + ":" + true, serializable);
            redisTemplate.rename(VIDEO_COUNT + videoID + ":" + true, VIDEO_COUNT + videoID + ":" + false);
        } catch (Throwable e) {
            //在上面的try之中找不到key的值 则走到这个代码块当中
            try {
                //认为这个字段已经被修改过了 则状态值字段为false
                VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + false);
                serializable.setViewCount(serializable.getViewCount() + 1);
                opsForValue.set(VIDEO_COUNT + videoID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "视频ID错误");
            }
        }
    }

    /**
     * 向Redis之中的点赞计数次数字段+1
     *
     * @param videoID
     */
    @Override
    public void addLike(Long videoID) {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        try {
            //当点赞视频时默认走到这里
            VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + true);
            serializable.setLikeCount(serializable.getLikeCount() + 1);
            opsForValue.set(VIDEO_COUNT + videoID + ":" + true, serializable);
            redisTemplate.rename(VIDEO_COUNT + videoID + ":" + true, VIDEO_COUNT + videoID + ":" + false);
        } catch (Throwable e) {
            //在上面的try之中找不到key的值 则走到这个代码块当中
            try {
                //认为这个字段已经被修改过了 则状态值字段为false
                VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + false);
                serializable.setLikeCount(serializable.getLikeCount() + 1);
                opsForValue.set(VIDEO_COUNT + videoID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "视频ID错误");
            }
        }
    }

    @Override
    public void removeLike(Long videoID) {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        try {
            //当点赞视频时默认走到这里
            VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + true);
            serializable.setLikeCount(serializable.getLikeCount() - 1);
            opsForValue.set(VIDEO_COUNT + videoID + ":" + true, serializable);
            redisTemplate.rename(VIDEO_COUNT + videoID + ":" + true, VIDEO_COUNT + videoID + ":" + false);
        } catch (Throwable e) {
            //在上面的try之中找不到key的值 则走到这个代码块当中
            try {
                //认为这个字段已经被修改过了 则状态值字段为false
                VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + false);
                serializable.setLikeCount(serializable.getLikeCount() - 1);
                opsForValue.set(VIDEO_COUNT + videoID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "视频ID错误");
            }
        }
    }

    /**
     * 向Redis之中的点踩计数次数字段+1
     */
    @Override
    public void addDisLike(Long videoID) {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        try {
            //当点赞视频时默认走到这里
            VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + true);
            serializable.setDislikeCount(serializable.getDislikeCount() + 1);
            opsForValue.set(VIDEO_COUNT + videoID + ":" + true, serializable);
            redisTemplate.rename(VIDEO_COUNT + videoID + ":" + true, VIDEO_COUNT + videoID + ":" + false);
        } catch (Throwable e) {
            //在上面的try之中找不到key的值 则走到这个代码块当中
            try {
                //认为这个字段已经被修改过了 则状态值字段为false
                VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + false);
                serializable.setDislikeCount(serializable.getDislikeCount() + 1);
                opsForValue.set(VIDEO_COUNT + videoID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "视频ID错误");
            }
        }
    }

    /**
     * 向Redis之中的点踩计数次数字段-1
     */
    @Override
    public void removeDisLike(Long videoID) {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        try {
            //当点赞视频时默认走到这里
            VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + true);
            serializable.setDislikeCount(serializable.getDislikeCount() - 1);
            opsForValue.set(VIDEO_COUNT + videoID + ":" + true, serializable);
            redisTemplate.rename(VIDEO_COUNT + videoID + ":" + true, VIDEO_COUNT + videoID + ":" + false);
        } catch (Throwable e) {
            //在上面的try之中找不到key的值 则走到这个代码块当中
            try {
                //认为这个字段已经被修改过了 则状态值字段为false
                VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + false);
                serializable.setDislikeCount(serializable.getDislikeCount() - 1);
                opsForValue.set(VIDEO_COUNT + videoID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "视频ID错误");
            }
        }
    }

    /**
     * 向Redis之中的弹幕计数次数字段+1
     */
    @Override
    public void addBarrageCount(Long videoID) {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        try {
            //当点赞视频时默认走到这里
            VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + true);
            serializable.setBarrageCount(serializable.getBarrageCount() + 1);
            opsForValue.set(VIDEO_COUNT + videoID + ":" + true, serializable);
            redisTemplate.rename(VIDEO_COUNT + videoID + ":" + true, VIDEO_COUNT + videoID + ":" + false);
        } catch (Throwable e) {
            //在上面的try之中找不到key的值 则走到这个代码块当中
            try {
                //认为这个字段已经被修改过了 则状态值字段为false
                VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + false);
                serializable.setBarrageCount(serializable.getBarrageCount() + 1);
                opsForValue.set(VIDEO_COUNT + videoID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "视频ID错误");
            }
        }
    }

    /**
     * 向Redis之中的弹幕计数次数字段-1
     */
    @Override
    public void removeBarrageCount(Long videoID) {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        try {
            //当点赞视频时默认走到这里
            VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + true);
            serializable.setBarrageCount(serializable.getBarrageCount() - 1);
            opsForValue.set(VIDEO_COUNT + videoID + ":" + true, serializable);
            redisTemplate.rename(VIDEO_COUNT + videoID + ":" + true, VIDEO_COUNT + videoID + ":" + false);
        } catch (Throwable e) {
            //在上面的try之中找不到key的值 则走到这个代码块当中
            try {
                //认为这个字段已经被修改过了 则状态值字段为false
                VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + false);
                serializable.setBarrageCount(serializable.getBarrageCount() - 1);
                opsForValue.set(VIDEO_COUNT + videoID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "视频ID错误");
            }
        }
    }

    /**
     * 向Redis之中的评论计数次数字段+1
     */
    @Override
    public void addCommentCount(Long videoID) {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        try {
            //当点赞视频时默认走到这里
            VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + true);
            serializable.setCommentCount(serializable.getCommentCount() + 1);
            opsForValue.set(VIDEO_COUNT + videoID + ":" + true, serializable);
            redisTemplate.rename(VIDEO_COUNT + videoID + ":" + true, VIDEO_COUNT + videoID + ":" + false);
        } catch (Throwable e) {
            //在上面的try之中找不到key的值 则走到这个代码块当中
            try {
                //认为这个字段已经被修改过了 则状态值字段为false
                VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + false);
                serializable.setCommentCount(serializable.getCommentCount() + 1);
                opsForValue.set(VIDEO_COUNT + videoID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "视频ID错误");
            }
        }
    }

    /**
     * 向Redis之中的评论计数次数字段-1
     */
    @Override
    public void removeCommentCount(Long videoID) {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        try {
            //当点赞视频时默认走到这里
            VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + true);
            serializable.setCommentCount(serializable.getCommentCount() - 1);
            opsForValue.set(VIDEO_COUNT + videoID + ":" + true, serializable);
            redisTemplate.rename(VIDEO_COUNT + videoID + ":" + true, VIDEO_COUNT + videoID + ":" + false);
        } catch (Throwable e) {
            //在上面的try之中找不到key的值 则走到这个代码块当中
            try {
                //认为这个字段已经被修改过了 则状态值字段为false
                VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + false);
                serializable.setCommentCount(serializable.getCommentCount() - 1);
                opsForValue.set(VIDEO_COUNT + videoID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "视频ID错误");
            }
        }
    }

    @Override
    public void addFavoriteCount(Long videoID) {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        try {
            //当点赞视频时默认走到这里
            VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + true);
            serializable.setFavoriteCount(serializable.getFavoriteCount() + 1);
            opsForValue.set(VIDEO_COUNT + videoID + ":" + true, serializable);
            redisTemplate.rename(VIDEO_COUNT + videoID + ":" + true, VIDEO_COUNT + videoID + ":" + false);
        } catch (Throwable e) {
            //在上面的try之中找不到key的值 则走到这个代码块当中
            try {
                //认为这个字段已经被修改过了 则状态值字段为false
                VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + false);
                serializable.setFavoriteCount(serializable.getFavoriteCount() + 1);
                opsForValue.set(VIDEO_COUNT + videoID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "视频ID错误");
            }
        }
    }

    @Override
    public void removeFavoriteCount(Long videoID) {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        try {
            //当点赞视频时默认走到这里
            VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + true);
            serializable.setFavoriteCount(serializable.getFavoriteCount() - 1);
            opsForValue.set(VIDEO_COUNT + videoID + ":" + true, serializable);
            redisTemplate.rename(VIDEO_COUNT + videoID + ":" + true, VIDEO_COUNT + videoID + ":" + false);
        } catch (Throwable e) {
            //在上面的try之中找不到key的值 则走到这个代码块当中
            try {
                //认为这个字段已经被修改过了 则状态值字段为false
                VideoCount serializable = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + false);
                serializable.setFavoriteCount(serializable.getFavoriteCount() - 1);
                opsForValue.set(VIDEO_COUNT + videoID + ":" + false, serializable);
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "视频ID错误");
            }
        }
    }

}
