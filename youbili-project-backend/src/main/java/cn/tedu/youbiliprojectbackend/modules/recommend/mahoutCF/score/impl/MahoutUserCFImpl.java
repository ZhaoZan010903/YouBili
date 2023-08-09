package cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.score.impl;


import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.dao.cache.DataMahoutCacheCF;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.mapper.MahoutMapper;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.pojo.UserArticleOperation;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.score.MahoutUserCF;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 协同过滤算法实现
 *
 * @author 赵錾
 */
@Repository
@Slf4j
public class MahoutUserCFImpl implements MahoutUserCF {

    @Autowired
    DataMahoutCacheCF dataMahoutCacheCF;

    @Autowired
    MahoutMapper mahoutMapper;


    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;


    /**
     * 定义一个 recommend 方法，接收一个用户 ID 作为参数，返回一个推荐项目 ID 的列表，
     * 抛出 TasteException 异常
     *
     * @param userID 用户ID
     * @return 推荐视频ID
     * @throws TasteException 相似度计算出错,数据模型问题等;
     */
    public List<Long> recommend(Long userID) throws TasteException {
        List<UserArticleOperation> selectuser = dataMahoutCacheCF.selectData();
        // 调用 createDataModel 方法，传入用户评分数据，返回一个 DataModel 对象，用于存储用户和项目的关系
        DataModel dataModel = this.createDataModel(selectuser);
        // 创建一个 UserSimilarity 对象，用于计算用户之间的相似度，使用 UncenteredCosineSimilarity 算法
        UserSimilarity similarity = new UncenteredCosineSimilarity(dataModel);
        // 创建一个 UserNeighborhood 对象，用于获取用户的邻居，使用 NearestNUserNeighborhood 算法，设置邻居数量为 30
        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100, similarity, dataModel);
        // 创建一个 Recommender 对象，用于生成推荐结果，使用 GenericUserBasedRecommender 算法，传入数据模型，用户邻居和用户相似度
        Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);
        // 调用 Recommender 的 recommend 方法，传入用户 ID 和推荐数量 12，返回一个 RecommendedItem 的列表
        List<RecommendedItem> recommendedItems = recommender.recommend(userID, 100);
        // 使用 Stream API 将 RecommendedItem 的列表转换为项目 ID 的列表
        List<Long> itemIds = recommendedItems.stream().map(RecommendedItem::getItemID).collect(Collectors.toList());
        /*
        由于目前数据样本量过小,为保证推荐视频数量,则添加随机方法
         */
        // 如果推荐项目的数量不足 12，则添加随机生成的项目 ID
        if (itemIds.size() <= 12) {
            log.debug("推荐数量不够,开始补充数量");
            Set<Long> newItemIds = new HashSet<>();
            Random random = new Random();
            while (12 - itemIds.size() >= newItemIds.size()) {
                Long ra = (long) (random.nextInt(30) + 1);
                newItemIds.add(ra);
            }
            for (Long newItemId : newItemIds) {
                itemIds.add(newItemId);
            }
        }
        // 如果推荐项目的数量超过 12，则删除项目 ID
        if (itemIds.size() > 12) {
            while (itemIds.size() > 12) {
                itemIds.remove(itemIds.size() - 1);
            }
        }
        return itemIds;
    }


    /**
     * 定义一个 createDataModel 方法，接收一个 UserArticleOperation 的列表作为参数，
     * 返回一个 DataModel 对象，抛出 TasteException 异常
     *
     * @param userArticleOperations 用户观看视频行为表
     * @return DataModel数据模型为对象
     */
    private DataModel createDataModel(List<UserArticleOperation> userArticleOperations) {
        // 创建一个 FastByIDMap 对象，该对象用于存储用户 ID 和对应用户的评分列表。
        FastByIDMap<PreferenceArray> fastByIdMap = new FastByIDMap<>();
        // 使用 Collectors.groupingBy() 方法将用户评分列表分组，根据用户 ID 进行分组。
        Map<Long, List<UserArticleOperation>> map = userArticleOperations.stream().collect(Collectors.groupingBy(UserArticleOperation::getUserID));
        // 获取分组后的用户评分列表的集合
        Collection<List<UserArticleOperation>> list = map.values();
        // 遍历每个用户的评分列表，并将每个评分转换为 GenericPreference 对象。
        for (List<UserArticleOperation> userPreferences : list) {
            // 创建一个 GenericPreference 的数组，用于存储每个用户的评分
            GenericPreference[] array = new GenericPreference[userPreferences.size()];
            for (int i = 0; i < userPreferences.size(); i++) {
                UserArticleOperation userPreference = userPreferences.get(i);
                // 创建一个 GenericPreference 对象，传入用户 ID，项目 ID 和评分值
                GenericPreference item = new GenericPreference(userPreference.getUserID(), userPreference.getVideoID(), userPreference.getValue());
                array[i] = item;
            }
            // 将数组转换为 GenericUserPreferenceArray 对象，并将其添加到 FastByIDMap 中，以用户 ID 作为键
            fastByIdMap.put(array[0].getUserID(), new GenericUserPreferenceArray(Arrays.asList(array)));
        }
        // 创建一个 GenericDataModel 对象，并传入 FastByIDMap 对象，返回该对象
        return new GenericDataModel(fastByIdMap);
    }


}
