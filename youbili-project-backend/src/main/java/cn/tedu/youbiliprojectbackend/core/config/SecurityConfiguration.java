package cn.tedu.youbiliprojectbackend.core.config;


import cn.tedu.youbiliprojectbackend.common.web.response.RestBean;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.core.filter.JwtAuthorizationFilter;
import cn.tedu.youbiliprojectbackend.modules.user.account.security.AccountServiceImpl;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;


/**
 * Security 6版本以后移除了继承 WebSecurityConfigurerAdapter
 *
 * @author 赵錾
 */
@Configuration
@Slf4j
@EnableWebSecurity(debug = false)
public class SecurityConfiguration {

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;


    /**
     * Security配置类
     *
     * @param http security
     * @return 返回配置结果
     * @throws Exception 错误
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /*配置HTTP请求的授权规则。它指定了/api/auth/**路径下的请求不需要认证，其他的请求都需要认证*/
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/api/auth/**").permitAll();
            auth.requestMatchers("/error/**").permitAll();
            auth.requestMatchers("/actuator/**").permitAll();
//            auth.requestMatchers("/alipay/notify");
            auth.requestMatchers("/alipay/**").permitAll();
            auth.requestMatchers("/orders/details/**").permitAll();
            auth.requestMatchers("/videos/handle/**").permitAll();
            auth.requestMatchers("/api/video/**").permitAll();
//            auth.requestMatchers("/index.html").permitAll();
            auth.anyRequest().authenticated();

        });
        /*来禁用跨站请求伪造防御*/
        http.csrf(AbstractHttpConfigurer::disable);
        /*
        这个方法用来启用跨域资源共享支持
        需要通过configurationSource()方法提供一个CorsConfigurationSource接口的实现类，
        来定义CORS的配置规则。
         */
        http.cors(cors -> cors.configurationSource(this.configurationSource()));
        /*用来配置会话管理策略。它指定了会话创建策略为无状态*/
        http.sessionManagement(sessionManage -> sessionManage.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        /*用来添加一个过滤器到过滤器链中*/
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        /*当用户尝试访问一个需要认证的资源时，如果没有提供有效的认证信息，该如何响应*/
        http.exceptionHandling(auth -> auth.authenticationEntryPoint(this::commence));
        /*退出登录. */
        http.logout(logout -> {
            logout.logoutUrl("/api/auth/logout");
            logout.logoutSuccessHandler(this::onLogoutSuccess);
        });
        return http.build();
    }


    /**
     * 退出登录成功时,响应结果;
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("退出登录成功!");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.success("退出登录成功")));
    }


    /**
     * 无权限访问的时候响应结果
     *
     * @param request       请求
     * @param response      响应
     * @param authException 错误
     * @throws IOException      IO错误
     * @throws ServletException 服务错误
     */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        log.debug("权限问题,未登录无权访问");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(ServiceCode.ERROR_FORBIDDEN)));
    }


    /**
     * 定义了一个 BCryptPasswordEncoder 的 bean，
     * 用于实现密码的加密和匹配。
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 定义了一个 AuthenticationManager 的 bean，
     * 用于实现认证管理。它使用了 HttpSecurity 对象来获取 AuthenticationManagerBuilder，
     * 并设置了用户详情服务（accountService）为 AuthorizeServiceImpl 对象。
     *
     * @param security
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity security) throws Exception {
        return security
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(accountService)
                .and()
                .build();
    }


    /**
     * Cors过滤配置
     */
    private CorsConfigurationSource configurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOriginPattern("*"); //上线后改为前端地址 不能写*
        cors.setAllowCredentials(true);    //允许携带cookie
        cors.addAllowedHeader("*");        //允许携带请求标头
        cors.addAllowedMethod("*");        //
        cors.addExposedHeader("*");        //
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

}

