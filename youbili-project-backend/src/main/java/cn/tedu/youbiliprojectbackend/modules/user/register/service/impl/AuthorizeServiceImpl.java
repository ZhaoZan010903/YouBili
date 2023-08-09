package cn.tedu.youbiliprojectbackend.modules.user.register.service.impl;

import cn.tedu.youbiliprojectbackend.common.ex.ServiceException;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.user.register.dao.cache.RegisteCacheRepository;
import cn.tedu.youbiliprojectbackend.modules.user.register.dao.mapper.UserMapper;
import cn.tedu.youbiliprojectbackend.modules.user.register.service.AuthorizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author 赵錾
 * @date 2023年6月6日13:37:38
 * <p>
 * 注册验证
 * <p>
 * <p>
 */
@Slf4j
@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    //获取yaml里面的spring.mail的username
    @Value("${spring.mail.username}")
    String from;

    //Mybatis
    @Autowired
    UserMapper mapper;

    //Spring-mail
    @Autowired
    MailSender mailSender;

    @Autowired
    RegisteCacheRepository registeCacheRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 1.先生成对应的验证码,
     * 2.把邮箱和对应的验证码放到Redis里面 (过期时间3分钟,如果此时要求重新发邮件,
     * 那么,只要剩余时间低于2分钟,就可以重新发送一次,重复此流程)
     * 3.发送验证码到指定邮箱
     * 4.如果发送失败,把Redis删除掉
     * 5.用户在注册时,再从Redis里面取出对应键值对,然后看验证码是否一直
     */
    @Override
    public void sendVaildateEmail(String email, String sessionId) {

        log.debug(" 开始发送验证码业务操作! ");

        String key = REGISTER_EMAIL_SEND + sessionId + " : " + email;
        log.debug("生成Key : {}", key);


        if (registeCacheRepository.keyByKey(key)) {  //判断这个hasKey是否存在
            Long expire = registeCacheRepository.timeOrdata(key); //获取key的过期时间，单位是秒，如果没有过期时间，返回0
            if (expire > 120) {
                throw new ServiceException(ServiceCode.ERROR, "请求频繁,请稍后再试");
            }
        }


        log.debug("没有存在继续执行! 继续执行");

        if (mapper.selectByEmail(email) != null) { // 判断邮箱是否已被注册
            throw new ServiceException(ServiceCode.ERROR, "此邮箱已被其他用户注册");
        }

        log.debug("正在生成验证码!");
        Random random = new Random();

        int code = random.nextInt(899999) + 100000;
        log.debug("验证码:{}", code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);            //设置发件人为from变量的值
        message.setTo(email);             //设置收件人为email变量的值
        message.setSubject("验证邮件");    //设置邮件主题为"验证邮件"
        message.setText("验证码是" + code);  //设置邮件内容为"验证码是"+code，即拼接验证码和字符串

        try {
            log.debug("开始发送邮件验证码");
            mailSender.send(message);
            registeCacheRepository.AddSendToEmail(key, String.valueOf(code));
//            template.opsForValue().set(key, String.valueOf(code), 3, TimeUnit.MINUTES); //存入redis数据库,持续时间3分钟
            log.debug("验证码为:{}", code);
//            return null;
        } catch (MailException e) {
            e.printStackTrace();
            throw new ServiceException(ServiceCode.ERROR_UNKNOWN, "邮件发送失败");
        }
    }

    /**
     * 注册操作
     * 邮箱+sessionId
     *
     * @param username  用户名
     * @param password  密码
     * @param email     邮箱
     * @param nickname  昵称
     * @param code      验证码
     * @param sessionId sessionId 也可以是唯一标识;
     */
    @Override
    public void validateAndRegister(String username, String password, String email, String nickname, String code, String sessionId) {

        log.debug("开始注册业务操作");
        String key = REGISTER_EMAIL_SEND + sessionId + " : " + email;
        if (registeCacheRepository.keyByKey(key)) {
            String s = registeCacheRepository.valueByKey(key);
            if (s == null) {
                throw new ServiceException(ServiceCode.ERROR, "验证码失效,请重新请求");
            }
            if (mapper.selectByUsername(username) != null) {
                throw new ServiceException(ServiceCode.ERROR, "用户名已被注册,请重新输入");
            }
            if (s.equals(code)) {
                password = passwordEncoder.encode(password);
                if (mapper.insertUser(username, password, email, nickname) > 0) {
//                    return null;
                } else {
                    throw new ServiceException(ServiceCode.ERROR, "注册错误");
                }
            } else {
                throw new ServiceException(ServiceCode.ERROR, "验证码错误,请检查后再提交");
            }
        } else {
            throw new ServiceException(ServiceCode.ERROR, "请先请求一封验证码邮件");
        }
    }


}
