package com.example.auth.jwt;


import com.example.auth.Entity.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.util.ArrayList;

@Slf4j
@Component
// 사용자가 Header 에 포함한 JWT 를 해석하고,
// 그에 따라 사용자가 인증된 상태인지를 확인하는 로드
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;

    public JwtTokenFilter(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // jwt 가 포함되어 있으면 포함되어 있는 헤더를 요청
        String authHeader
                = request.getHeader(HttpHeaders.AUTHORIZATION);
        // authHeater 가 null 이 아니면서 "Bearer " 로 구성되어 있어야
        // 정상적인 인증 정보다.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // jwt 를 회수하여 jwt 가 정상적인 jwt 인지를 판단한다.
            String token = authHeader.split(" ")[1];
            if (jwtTokenUtils.validate(token)) {
                // 웹상의 많은 예시
//                SecurityContextHolder.getContext().setAuthentication();
                // Security 공식 문서 추천
                SecurityContext context
                        = SecurityContextHolder.createEmptyContext();
                // JWT에서 사용자 이름을 가져오기
                String username = jwtTokenUtils
                        .parseClaims(token)
                        .getSubject();
                // 사용자 인증 정보 생성
                AbstractAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(
                        CustomUserDetails.builder()
                                .username(username)
                                .build(),
                        token, new ArrayList<>()
                );
                // SecurityContext에 사용자 정보 설정
                context.setAuthentication(authenticationToken);
                // SecurityContextHolder에 SecurityContext 설정
                SecurityContextHolder.setContext(context);
                log.info("set security context with jwt");
            }
            // 아니라면 log.warn을 통해 알린다.
            else {
                log.warn("jwt validation failed");
            }
        }
        filterChain.doFilter(request, response);
    }
}
