package cn.tedu.youbiliprojectbackend.modules.search.service.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.modules.search.pojo.param.VideoSearchParam;
import cn.tedu.youbiliprojectbackend.modules.search.pojo.vo.VideoSearchVO;
import cn.tedu.youbiliprojectbackend.modules.search.service.IVideoSearchService;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static cn.tedu.youbiliprojectbackend.common.consts.VideoConsts.VIDEO_LIST;

@Service
@Slf4j
public class VideoSearchServiceImpl implements IVideoSearchService {
    @Autowired
    ElasticsearchClient client;


    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;


    @Override
    public void rebuildSearch() {
        log.debug("开始处理【更新搜索服务器中的视频列表】的业务，无参数");
        try {
            //要更新搜索服务器视频列表,首先可以删除该索引
            //判断要存的索引存不存在,存在的话删除后新建,不存在直接新建索引video
            Boolean indexExist = client.indices().exists(e -> e.index("videos")).value();
            if (indexExist) {
                log.debug("索引存在,即将删除");
                client.indices().delete(d -> d.index("videos"));
                log.debug("索引删除");
            }

            //从Redis查出来需要的数据并创建索引video
            //es里面存储的每条数据的id都是userID_videoID形式
            List<BulkOperation> bulkOperationArrayList = new ArrayList<>();
            ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
            Set<String> keys = redisTemplate.keys(VIDEO_LIST + "*:*");
            for (String key : keys) {
                VideoUserCache videoUserCache = (VideoUserCache) opsForValue.get(key);
                bulkOperationArrayList.add(BulkOperation.of(o -> o.index(i -> i.document(videoUserCache).id(videoUserCache.getUserID() + "_" + videoUserCache.getVideoID()))));
            }


            //创建的索引名称为video
            BulkResponse bulkResponse = client.bulk(b -> b.index("videos")
                    .operations(bulkOperationArrayList));
            log.debug("创建索引成功");

        } catch (IOException e) {
            //捕获到异常可以什么也不做
        }
    }

    @Override
    public List<VideoSearchVO> searchByTitle(String title) {
        log.debug("开始处理【根据视频标题对视频列表进行搜索】的业务，{}", title);
        List<VideoSearchVO> videoSearchVOList = new ArrayList<>();
        try {
            SearchResponse<VideoSearchVO> searchResponse = client.search(s -> s.index("videos")
                            .query(q -> q
                                    .match(t -> t
                                            .field("title")
                                            .query(title)
                                    ))
                            // 高亮查询
                            .highlight(highlightBuilder -> highlightBuilder
                                    .preTags("<font color='red'>")
                                    .postTags("</font>")
                                    .requireFieldMatch(false) //多字段时，需要设置为false
                                    .fields("title", highlightFieldBuilder -> highlightFieldBuilder)
                            ),
                    VideoSearchVO.class);
            List<Hit<VideoSearchVO>> hitList = searchResponse.hits().hits();
            System.out.println(hitList);

            for (Hit<VideoSearchVO> mapHit : hitList) {
                videoSearchVOList.add(mapHit.source());
            }

        } catch (IOException e) {
            //捕获到异常可以什么也不做
        }
        return videoSearchVOList;
    }


    @Override
    public void insertIntoEs(VideoSearchParam videoSearchParam) {
        log.debug("开始处理向es插入数据的业务");
        try {
            IndexResponse indexResponse = client.index(i -> i
                    .index("videos")
                    //设置id
                    .id(videoSearchParam.getUserID() + "_" + videoSearchParam.getVideoID())
                    //传入user对象
                    .document(videoSearchParam));
        } catch (IOException e) {
            //什么也不做
        }
    }


    //视频发生修改
    @Override
    public void update(VideoSearchParam videoSearchParam) {
        String id = videoSearchParam.getUserID() + "_" + videoSearchParam.getVideoID();
        try {
            if (client.exists(e -> e.index("videos").id(id)).value()) {
                UpdateResponse<VideoSearchParam> updateResponse = client.update(u -> u
                                .index("videos")
                                .id(id)
                                .doc(videoSearchParam)
                        , VideoSearchParam.class);
            }

        } catch (IOException e) {
            //什么也不做
        }
    }

    //视频下架
    @Override
    public void deleteByUserIDAndVideoID(Long userID, Long videoID) {
        String id = userID + "_" + videoID;
        try {
            if (client.exists(e -> e.index("videos").id(id)).value()) {
                DeleteResponse deleteResponse = client.delete(d -> d
                        .index("videos")
                        .id(id)
                );
            }

        } catch (IOException e) {
            //
        }
    }


}
