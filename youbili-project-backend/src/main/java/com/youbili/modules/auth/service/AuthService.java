package com.youbili.modules.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.youbili.common.constant.CacheKeys;
import com.youbili.common.exception.ServiceException;
import com.youbili.common.response.ServiceCode;
import com.youbili.common.security.CustomUserDetails;
import com.youbili.common.security.JwtTokenProvider;
import com.youbili.modules.auth.dto.LoginRequest;
import com.youbili.modules.auth.dto.RegisterRequest;
import com.youbili.modules.auth.vo.LoginResponseVO;
import com.youbili.modules.user.entity.User;
import com.youbili.modules.user.mapper.UserMapper;
import com.youbili.modules.user.vo.UserProfileVO;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String DEFAULT_AVATAR = "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png";

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate stringRedisTemplate;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    public LoginResponseVO login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername().trim(), request.getPassword())
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);
        userMapper.updateLastLoginDate(userDetails.getUserId());

        UserProfileVO profile = getProfile(userDetails.getUserId());
        return LoginResponseVO.builder()
                .token(token)
                .tokenType("Bearer")
                .user(profile)
                .build();
    }

    public void logout(String rawToken) {
        if (!StringUtils.hasText(rawToken)) {
            return;
        }
        try {
            Claims claims = jwtTokenProvider.parseClaims(rawToken);
            String tokenId = claims.getId();
            long remainingSeconds = jwtTokenProvider.getRemainingSeconds(claims);
            if (StringUtils.hasText(tokenId) && remainingSeconds > 0) {
                stringRedisTemplate.opsForValue().set(
                        CacheKeys.AUTH_BLACKLIST_PREFIX + tokenId,
                        "1",
                        Duration.ofSeconds(remainingSeconds)
                );
            }
        } catch (Exception e) {
            log.debug("Skipping token revocation during logout: {}", e.getMessage());
        }
    }

    public void sendVerificationEmail(String rawEmail) {
        String email = rawEmail.trim().toLowerCase(Locale.ROOT);
        String cooldownKey = CacheKeys.EMAIL_COOLDOWN_PREFIX + email;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(cooldownKey))) {
            throw new ServiceException(ServiceCode.TOO_MANY_REQUESTS, "验证码发送过于频繁，请 60 秒后再试");
        }

        boolean emailExists = userMapper.exists(
                new LambdaQueryWrapper<User>().eq(User::getEmail, email)
        );
        if (emailExists) {
            throw new ServiceException(ServiceCode.CONFLICT, "该邮箱已被注册");
        }

        if (!StringUtils.hasText(mailFrom)) {
            throw new ServiceException(ServiceCode.MAIL_ERROR, "管理员暂未配置 SMTP 邮件服务，请联系管理员或使用演示账号登录");
        }

        String code = String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(email);
        message.setSubject("YouBili 注册验证码");
        message.setText("欢迎注册 YouBili！您的邮箱验证码为：" + code + "，5 分钟内有效，请勿泄露给他人。");

        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Failed to send verification email to {}", email, e);
            throw new ServiceException(ServiceCode.MAIL_ERROR, "验证码邮件发送失败，请稍后重试");
        }

        stringRedisTemplate.opsForValue().set(CacheKeys.EMAIL_CODE_PREFIX + email, code, Duration.ofMinutes(5));
        stringRedisTemplate.opsForValue().set(cooldownKey, "1", Duration.ofSeconds(60));
    }

    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest request) {
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        String codeKey = CacheKeys.EMAIL_CODE_PREFIX + email;

        String cachedCode = stringRedisTemplate.opsForValue().get(codeKey);
        if (!StringUtils.hasText(cachedCode)) {
            throw new ServiceException(ServiceCode.BAD_REQUEST, "请先获取邮箱验证码或验证码已过期");
        }
        if (!cachedCode.equals(request.getCode().trim())) {
            throw new ServiceException(ServiceCode.BAD_REQUEST, "验证码错误，请检查后重试");
        }

        if (userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getUsername, username))) {
            throw new ServiceException(ServiceCode.CONFLICT, "用户名已被占用");
        }
        if (userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getEmail, email))) {
            throw new ServiceException(ServiceCode.CONFLICT, "该邮箱已被注册");
        }

        User user = new User();
        user.setUsername(username);
        user.setNickname(request.getNickname().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(email);
        user.setAccountLevel(1);
        user.setFans(0);
        user.setFollows(0);
        user.setVideoCount(0);
        user.setTotalLikes(0);
        user.setMembershipStatus("Inactive");
        user.setRegistrationDate(LocalDateTime.now());
        user.setImgUrl(DEFAULT_AVATAR);
        user.setEnable(1);

        userMapper.insert(user);
        stringRedisTemplate.delete(codeKey);
    }

    public UserProfileVO getProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "用户不存在");
        }
        return toProfileVO(user);
    }

    public static UserProfileVO toProfileVO(User user) {
        return UserProfileVO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .avatarUrl(StringUtils.hasText(user.getImgUrl()) ? user.getImgUrl() : DEFAULT_AVATAR)
                .birthday(user.getBirthday())
                .accountLevel(user.getAccountLevel() == null ? 1 : user.getAccountLevel())
                .fans(user.getFans() == null ? 0 : user.getFans())
                .follows(user.getFollows() == null ? 0 : user.getFollows())
                .videoCount(user.getVideoCount() == null ? 0 : user.getVideoCount())
                .totalLikes(user.getTotalLikes() == null ? 0 : user.getTotalLikes())
                .membershipStatus(user.getMembershipStatus())
                .registrationDate(user.getRegistrationDate())
                .build();
    }
}
