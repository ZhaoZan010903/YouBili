package cn.tedu.youbiliprojectbackend.modules.user.account.dao.mapper;


import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.entity.Account;
import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.vo.AccountMeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author 赵錾
 */
@Repository
public interface AccountMapper extends BaseMapper<Account> {
    Account SelectByUserName(String username);

    AccountMeVO SelectByMe(Long userID);

}
