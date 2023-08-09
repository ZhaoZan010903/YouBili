package cn.tedu.youbiliprojectbackend.modules.video.barrage.server;

import cn.tedu.youbiliprojectbackend.modules.video.barrage.pojo.DTO.BarrageDTO;
import cn.tedu.youbiliprojectbackend.modules.video.barrage.pojo.VO.BarrageVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarrageService {
    List<BarrageVO> findByVideoID(Long videoID);

    void sent(BarrageDTO barrageDTO);
}
