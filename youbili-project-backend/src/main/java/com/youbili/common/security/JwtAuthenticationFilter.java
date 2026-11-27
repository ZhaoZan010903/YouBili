package com.youbili.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youbili.common.constant.CacheKeys;
import com.youbili.common.response.ApiResponse;
import com.youbili.common.response.ServiceCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtTokenProvider.parseClaims(token);
            String tokenId = claims.getId();

            if (StringUtils.hasText(tokenId) && isTokenRevoked(tokenId)) {
                writeError(response, HttpServletResponse.SC_UNAUTHORIZED, ServiceCode.JWT_EXPIRED);
                return;
            }

            Long userId = claims.get("userId", Long.class);
            String username = claims.getSubject();
            List<?> rawRoles = claims.get("roles", List.class);
            List<SimpleGrantedAuthority> authorities = rawRoles == null
                    ? Collections.emptyList()
                    : rawRoles.stream()
                        .map(String::valueOf)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

            CurrentPrincipal principal = new CurrentPrincipal(userId, username, tokenId);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.debug("JWT expired for request {}", request.getRequestURI());
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, ServiceCode.JWT_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT for request {}: {}", request.getRequestURI(), e.getMessage());
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, ServiceCode.JWT_INVALID);
        }
    }

    public static String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(header)) {
            return null;
        }
        String trimmed = header.trim();
        if (trimmed.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
            return trimmed.substring(BEARER_PREFIX.length()).trim();
        }
        return trimmed;
    }

    private boolean isTokenRevoked(String tokenId) {
        try {
            return Boolean.TRUE.equals(
                    stringRedisTemplate.hasKey(CacheKeys.AUTH_BLACKLIST_PREFIX + tokenId)
            );
        } catch (Exception e) {
            log.warn("Redis unavailable while checking token revocation: {}", e.getMessage());
            return false;
        }
    }

    private void writeError(HttpServletResponse response, int httpStatus, ServiceCode code) throws IOException {
        response.setStatus(httpStatus);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.failure(code)));
    }
}
