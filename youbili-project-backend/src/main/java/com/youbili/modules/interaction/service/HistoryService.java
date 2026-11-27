package com.youbili.modules.interaction.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.youbili.common.exception.ServiceException;
import com.youbili.common.response.ServiceCode;
import com.youbili.modules.interaction.dto.RecordHistoryRequest;
import com.youbili.modules.interaction.entity.WatchHistory;
import com.youbili.modules.interaction.mapper.HistoryMapper;
import com.youbili.modules.interaction.vo.HistoryItemVO;
import com.youbili.modules.video.mapper.VideoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryMapper historyMapper;
    private final VideoMapper videoMapper;

    public List<HistoryItemVO> listHistory(Long userId, String keyword) {
        return historyMapper.selectHistoryList(userId, keyword == null ? null : keyword.trim());
    }

    @Transactional(rollbackFor = Exception.class)
    public void recordWatch(Long userId, RecordHistoryRequest request) {
        int seconds = request.getWatchDurationSeconds() == null ? 0 : Math.min(request.getWatchDurationSeconds(), 86399);
        LocalTime duration = LocalTime.ofSecondOfDay(seconds);

        WatchHistory existing = historyMapper.selectOne(
                new LambdaQueryWrapper<WatchHistory>()
                        .eq(WatchHistory::getUserId, userId)
                        .eq(WatchHistory::getVideoId, request.getVideoId())
        );
        if (existing == null) {
            WatchHistory history = new WatchHistory();
            history.setUserId(userId);
            history.setVideoId(request.getVideoId());
            history.setWatchDate(LocalDateTime.now());
            history.setWatchDuration(duration);
            history.setEnable(1);
            historyMapper.insert(history);
            videoMapper.incrementViewCount(request.getVideoId());
        } else {
            boolean countNewView = existing.getWatchDate() == null
                    || existing.getWatchDate().isBefore(LocalDateTime.now().minusMinutes(10));
            existing.setWatchDate(LocalDateTime.now());
            existing.setWatchDuration(duration);
            existing.setEnable(1);
            historyMapper.updateById(existing);
            if (countNewView) {
                videoMapper.incrementViewCount(request.getVideoId());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteOne(Long userId, Long historyId) {
        WatchHistory existing = historyMapper.selectById(historyId);
        if (existing == null || !Integer.valueOf(1).equals(existing.getEnable())) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "历史记录不存在");
        }
        if (!Objects.equals(existing.getUserId(), userId)) {
            throw new ServiceException(ServiceCode.FORBIDDEN, "无权删除他人历史记录");
        }
        existing.setEnable(0);
        historyMapper.updateById(existing);
    }

    @Transactional(rollbackFor = Exception.class)
    public void clearAll(Long userId) {
        historyMapper.clearAllByUserId(userId);
    }
}
