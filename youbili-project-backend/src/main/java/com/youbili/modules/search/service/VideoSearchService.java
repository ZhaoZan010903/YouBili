package com.youbili.modules.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youbili.common.response.PageResult;
import com.youbili.modules.video.mapper.VideoMapper;
import com.youbili.modules.video.vo.VideoCardVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoSearchService {

    private static final String INDEX_NAME = "youbili_videos";

    private final ObjectProvider<ElasticsearchClient> esClientProvider;
    private final VideoMapper videoMapper;

    public PageResult<VideoCardVO> search(String keyword, int page, int size, String sort) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 60);
        String trimmed = keyword == null ? "" : keyword.trim();

        ElasticsearchClient esClient = esClientProvider.getIfAvailable();
        if (esClient != null && StringUtils.hasText(trimmed)) {
            try {
                SearchResponse<VideoCardVO> response = esClient.search(s -> {
                    s.index(INDEX_NAME)
                            .from((safePage - 1) * safeSize)
                            .size(safeSize)
                            .query(q -> q.multiMatch(m -> m
                                    .fields("title^3", "description", "authorNickname^2")
                                    .query(trimmed)));
                    if ("views".equalsIgnoreCase(sort)) {
                        s.sort(so -> so.field(f -> f.field("viewCount").order(SortOrder.Desc)));
                    } else if ("likes".equalsIgnoreCase(sort)) {
                        s.sort(so -> so.field(f -> f.field("likeCount").order(SortOrder.Desc)));
                    }
                    return s;
                }, VideoCardVO.class);

                List<VideoCardVO> list = new ArrayList<>();
                for (Hit<VideoCardVO> hit : response.hits().hits()) {
                    if (hit.source() != null) {
                        list.add(hit.source());
                    }
                }
                long total = response.hits().total() == null ? list.size() : response.hits().total().value();
                return PageResult.of(safePage, safeSize, total, list);
            } catch (Exception e) {
                log.warn("Elasticsearch search failed, falling back to PostgreSQL: {}", e.getMessage());
            }
        }

        IPage<VideoCardVO> dbPage = videoMapper.searchVideosByKeyword(
                new Page<>(safePage, safeSize), trimmed, sort
        );
        return PageResult.of(dbPage.getCurrent(), dbPage.getSize(), dbPage.getTotal(), dbPage.getRecords());
    }

    public List<String> suggest(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return Collections.emptyList();
        }
        return videoMapper.suggestTitles(keyword.trim(), 8);
    }

    public void syncVideo(Long videoId) {
        ElasticsearchClient esClient = esClientProvider.getIfAvailable();
        if (esClient == null || videoId == null) {
            return;
        }
        try {
            ensureIndexExists(esClient);
            VideoCardVO card = videoMapper.selectVideoCardById(videoId);
            if (card != null && "Approved".equals(card.getReviewStatus())) {
                esClient.index(i -> i.index(INDEX_NAME).id(String.valueOf(videoId)).document(card));
            }
        } catch (Exception e) {
            log.warn("Failed to index video {} in Elasticsearch: {}", videoId, e.getMessage());
        }
    }

    public void removeVideo(Long videoId) {
        ElasticsearchClient esClient = esClientProvider.getIfAvailable();
        if (esClient == null || videoId == null) {
            return;
        }
        try {
            esClient.delete(d -> d.index(INDEX_NAME).id(String.valueOf(videoId)));
        } catch (Exception e) {
            log.debug("Failed to delete video {} from Elasticsearch: {}", videoId, e.getMessage());
        }
    }

    @Scheduled(fixedDelay = 1_800_000L, initialDelay = 15_000L)
    public void syncElasticsearchIndex() {
        ElasticsearchClient esClient = esClientProvider.getIfAvailable();
        if (esClient == null) {
            return;
        }
        try {
            ensureIndexExists(esClient);
            List<VideoCardVO> videos = videoMapper.selectApprovedCards(2000);
            if (videos.isEmpty()) {
                return;
            }
            List<BulkOperation> ops = videos.stream()
                    .map(v -> BulkOperation.of(b -> b.index(idx -> idx
                            .index(INDEX_NAME)
                            .id(String.valueOf(v.getVideoId()))
                            .document(v))))
                    .toList();
            BulkResponse response = esClient.bulk(b -> b.index(INDEX_NAME).operations(ops));
            if (response.errors()) {
                log.warn("Elasticsearch bulk sync completed with partial errors");
            } else {
                log.info("Synced {} videos to Elasticsearch index '{}'", videos.size(), INDEX_NAME);
            }
        } catch (Exception e) {
            log.warn("Elasticsearch periodic sync skipped: {}", e.getMessage());
        }
    }

    private void ensureIndexExists(ElasticsearchClient esClient) throws Exception {
        boolean exists = esClient.indices().exists(e -> e.index(INDEX_NAME)).value();
        if (!exists) {
            esClient.indices().create(c -> c.index(INDEX_NAME));
            log.info("Created Elasticsearch index '{}'", INDEX_NAME);
        }
    }
}
