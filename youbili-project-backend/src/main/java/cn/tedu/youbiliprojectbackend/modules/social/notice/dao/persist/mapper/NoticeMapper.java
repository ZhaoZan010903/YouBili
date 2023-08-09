package cn.tedu.youbiliprojectbackend.modules.social.notice.dao.persist.mapper;

import cn.tedu.youbiliprojectbackend.modules.social.notice.pojo.entity.Notice;
import cn.tedu.youbiliprojectbackend.modules.social.notice.pojo.vo.NoticeAllListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeMapper extends BaseMapper<Notice> {

    List<NoticeAllListVO> userEnableNoticeList(Integer enable);

    List<NoticeAllListVO> adminEnableNoticeList(Integer enable);

    List<NoticeAllListVO> userNoticeUserId(Long userID);

    void deleteByUserId(Long noticeID);


}
