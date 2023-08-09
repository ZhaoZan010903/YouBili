package cn.tedu.youbiliprojectbackend.modules.user.register.dao.mapper;


import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.entity.Account;
import cn.tedu.youbiliprojectbackend.modules.user.register.pojo.vo.UserRegist;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
@TableName("user")
public interface UserMapper extends BaseMapper<Account> {

    //注册
    int insertUser(String username, String password, String email, String nickname);

    //查询邮箱
    UserRegist selectByEmail(String email);

    UserRegist selectByUsername(String username);

}
