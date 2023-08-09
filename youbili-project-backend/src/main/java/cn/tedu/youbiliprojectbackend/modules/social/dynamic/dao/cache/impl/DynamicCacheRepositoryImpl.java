package cn.tedu.youbiliprojectbackend.modules.social.dynamic.dao.cache.impl;


import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.common.consts.VideoConsts;
import cn.tedu.youbiliprojectbackend.common.utils.pagedate.PageData;
import cn.tedu.youbiliprojectbackend.modules.social.dynamic.dao.cache.IDynamicCacheRepository;
import cn.tedu.youbiliprojectbackend.modules.social.dynamic.pojo.vo.DynamicListVO;
import cn.tedu.youbiliprojectbackend.modules.user.follow.pojo.vo.FollowListVO;
import cn.tedu.youbiliprojectbackend.modules.user.follow.service.IFollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 处理类别数据的缓存的存储库实现类
 *
 * @author java@tedu.cn
 * @version 1.0
 */
@Slf4j
@Repository
public class DynamicCacheRepositoryImpl implements IDynamicCacheRepository, VideoConsts {

    public static String uploadDate = "";
    public static String duration = "";
    @Autowired
    IFollowService followService;
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public List<DynamicListVO> list(Long userID) {
        log.debug("开始处理【从缓存中读取类别列表】的数据访问，无参数");
        //当前用户的关注列表
        List<FollowListVO> list = followService.list(userID);
        List<DynamicListVO> dynamicListVOS = new ArrayList<>();
        for (FollowListVO followListVO : list) {
            Long followID = followListVO.getFollowedID();
            Set<String> keys = redisTemplate.keys(VIDEO_LIST + followID + ":*");
            for (String key : keys) {
                DynamicListVO dynamicListVO = new DynamicListVO();
                VideoUserCache serializable = (VideoUserCache) redisTemplate.opsForValue().get(key);
                System.out.println(serializable);
                if (serializable != null) {
                    BeanUtils.copyProperties(serializable, dynamicListVO);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //转换发布时间格式

                    Date dateTime = serializable.getDuration();
                    System.out.println(dateTime);
                    conversionUploadDate(serializable);
                    dynamicListVO.setUploadDate(uploadDate);
//                    //转换视频时长格式"Fri Jan 02 00:11:23 CST 1970" to 11:23
//                    conversionDuration(serializable);
//                    dynamicListVO.setDuration(duration);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    dynamicListVOS.add(dynamicListVO);
                }
            }
        }
        System.out.println(dynamicListVOS);
        return dynamicListVOS;
    }

    private void conversionDuration(VideoUserCache serializable) {
        String timestamp = serializable.getDuration().toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        ZonedDateTime pastTime = ZonedDateTime.parse(timestamp, formatter);
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));

        Duration duration = Duration.between(pastTime, currentTime);

        if (duration.toMinutes() < 60) {
            long minutes = duration.toMinutes();
            uploadDate = minutes + "分钟前";
        } else if (duration.toHours() < 24) {
            long hours = duration.toHours();
            uploadDate = hours + "小时前";
        } else {
            LocalDate date = pastTime.toLocalDate();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = dateFormatter.format(date);
            uploadDate = formattedDate + " ";
        }
    }

    private void conversionUploadDate(VideoUserCache serializable) {
        String timestamp = serializable.getUploadDate().toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        ZonedDateTime pastTime = ZonedDateTime.parse(timestamp, formatter);
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));

        Duration duration = Duration.between(pastTime, currentTime);

        if (duration.toMinutes() < 60) {
            long minutes = duration.toMinutes();
            uploadDate = minutes + "分钟前";
        } else if (duration.toHours() < 24) {
            long hours = duration.toHours();
            uploadDate = hours + "小时前";
        } else {
            LocalDate date = pastTime.toLocalDate();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = dateFormatter.format(date);
            uploadDate = formattedDate + " ";
        }
    }


    @Override
    public PageData<DynamicListVO> list(Long userID, Integer pageNum, Integer pageSize) {
        log.debug("开始执行【查询文章列表】的数据访问，页码：{}，每页记录数：{}", pageNum, pageSize);
        ListOperations<String, Serializable> opsForList = redisTemplate.opsForList();

        long start = (long) (pageNum - 1) * pageSize;
        long end = (long) pageNum * pageSize - 1;
        List<Serializable> range = opsForList.range(VIDEO_LIST + userID + "*", start, end);

        List<DynamicListVO> categoryListItemVOList = new ArrayList<>();
        for (Serializable serializable : range) {
            categoryListItemVOList.add((DynamicListVO) serializable);
        }

        long total = opsForList.size(VIDEO_LIST + userID + "*");
        // long maxPage = (total % pageSize == 0) ? (total / pageSize) : (total / pageSize + 1);
        long maxPage = total / pageSize;
        if (total % pageSize != 0) {
            maxPage++;
        }

        PageData<DynamicListVO> pageData = new PageData<>();
        pageData.setList(categoryListItemVOList)
                .setCurrentPage(pageNum)
                .setMaxPage((int) maxPage)
                .setPageSize(pageSize)
                .setTotal(total);
        return pageData;
    }
}
