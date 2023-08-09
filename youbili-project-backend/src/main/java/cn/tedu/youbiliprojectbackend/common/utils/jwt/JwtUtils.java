package cn.tedu.youbiliprojectbackend.common.utils.jwt;


import cn.tedu.youbiliprojectbackend.modules.user.account.security.CustomUserDetails;
import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 这是一个Jwt的工具类,其中包含了生成JWT的方法,和解析JWT的方法;
 */
@Slf4j
public class JwtUtils {
    @Value("${youbili.jwt.secret-key}")
    private final static String secretKey = "fNesMDkqrJFdsfDSwAbFLJ8SnsHJ438AF72D73aKJSmfdsafdLKKAFKDSJ";

    @Value("${youbili.jwt.duration-in-minute}")
    private final static Long durationInMinute = 86400L;

    public static String createJWT(CustomUserDetails userDetails) {

        Long userID = userDetails.getUserID();
        log.debug("从认证结果中的当事人中获取ID：{}", userID);

        String username = userDetails.getUsername();
        log.debug("从认证结果中的当事人中获取用户名：{}", username);

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
//        log.debug("从认证结果中的当事人中获取权限列表：{}", authorities);

        String authoritiesJsonString = JSON.toJSONString(authorities);
//        log.debug("将权限列表对象转换为JSON格式的字符串：{}", authoritiesJsonString);

        Date date = new Date(System.currentTimeMillis() + 60 * 1000 * durationInMinute);
        //                                                 ↑ 注意加L，避免int溢出为负数
        Map<String, Object> claims = new HashMap<>();
        claims.put("userID", userID);
        claims.put("username", username);
        claims.put("authoritiesJsonString", authoritiesJsonString);
        String jwt = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        log.debug("生成的JWT为: {}", jwt);

        return jwt;
    }

    public static Claims parseToken(String Token) throws JwtException {
        log.debug("开始解析JWT");
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(Token)
                .getBody();
        return claims;

    }

}