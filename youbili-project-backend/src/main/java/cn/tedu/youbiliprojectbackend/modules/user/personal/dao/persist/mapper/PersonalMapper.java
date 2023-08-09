package cn.tedu.youbiliprojectbackend.modules.user.personal.dao.persist.mapper;

import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.entity.Account;
import cn.tedu.youbiliprojectbackend.modules.user.personal.pojo.vo.CenterVideoInfoVO;
import cn.tedu.youbiliprojectbackend.modules.user.personal.pojo.vo.PersonalCenterUserInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalMapper extends BaseMapper<Account> {
    List<CenterVideoInfoVO> selectVideoInfoByUserId(Long id);

    List<CenterVideoInfoVO> selectFavoriteInfoByUserId(Long id);

    List<CenterVideoInfoVO> selectHistoryInfoByUserId(Long id);

    PersonalCenterUserInfoVO selectUserInfoByUserId(Long id);
}
