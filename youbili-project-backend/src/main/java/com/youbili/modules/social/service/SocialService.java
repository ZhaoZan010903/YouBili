package com.youbili.modules.social.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youbili.common.exception.ServiceException;
import com.youbili.common.response.PageResult;
import com.youbili.common.response.ServiceCode;
import com.youbili.modules.social.dto.CreateReportRequest;
import com.youbili.modules.social.entity.Notice;
import com.youbili.modules.social.entity.Report;
import com.youbili.modules.social.mapper.NoticeMapper;
import com.youbili.modules.social.mapper.ReportMapper;
import com.youbili.modules.video.mapper.VideoMapper;
import com.youbili.modules.video.vo.VideoCardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final VideoMapper videoMapper;
    private final NoticeMapper noticeMapper;
    private final ReportMapper reportMapper;

    public PageResult<VideoCardVO> listDynamics(Long userId, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 50);
        IPage<VideoCardVO> result = videoMapper.selectDynamicFeed(new Page<>(safePage, safeSize), userId);
        return PageResult.of(result.getCurrent(), result.getSize(), result.getTotal(), result.getRecords());
    }

    public List<Notice> listNotices(Long userId, String type) {
        if ("system".equalsIgnoreCase(type)) {
            return noticeMapper.selectList(
                    new LambdaQueryWrapper<Notice>()
                            .eq(Notice::getTypeEnable, 0)
                            .orderByDesc(Notice::getNoticeTime, Notice::getNoticeId)
            );
        }
        return noticeMapper.selectList(
                new LambdaQueryWrapper<Notice>()
                        .eq(Notice::getUserId, userId)
                        .eq(Notice::getTypeEnable, 1)
                        .orderByDesc(Notice::getNoticeTime, Notice::getNoticeId)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void markNoticeRead(Long userId, Long noticeId) {
        Notice notice = noticeMapper.selectById(noticeId);
        if (notice == null) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "通知不存在");
        }
        if (notice.getUserId() != null && !Objects.equals(notice.getUserId(), userId)) {
            throw new ServiceException(ServiceCode.FORBIDDEN, "无权操作他人通知");
        }
        notice.setIsRead(1);
        noticeMapper.updateById(notice);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePersonalNotice(Long userId, Long noticeId) {
        Notice notice = noticeMapper.selectById(noticeId);
        if (notice == null) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "通知不存在");
        }
        if (!Integer.valueOf(1).equals(notice.getTypeEnable()) || !Objects.equals(notice.getUserId(), userId)) {
            throw new ServiceException(ServiceCode.FORBIDDEN, "无权删除该通知");
        }
        noticeMapper.deleteById(noticeId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long submitReport(Long userId, CreateReportRequest request) {
        Report report = new Report();
        report.setUserId(userId);
        report.setTargetType(request.getTargetType());
        report.setTargetId(request.getTargetId());
        report.setCategory(request.getCategory().trim());
        report.setReasons(request.getReasons() == null ? "" : String.join(",", request.getReasons()));
        report.setDescription(request.getDescription().trim());
        report.setStatus("Pending");
        reportMapper.insert(report);
        return report.getReportId();
    }
}
