package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// 5.7 버전 이전 : extends WebSecurityConfigureAdapter
// 6.1 버전 이후 : Builder -> Lambda 이용 DSL 기반 설정

@Configuration
// @EnableWebSecurity  // 2.1 버전 이후로 Spring Boot Starter Security 에서는 필수 x
public class WebSecurityConfig {
    @Bean  // 메소드의 결과를 BEAN 객체로 등록해주는 어노테이션
    public SecurityFilterChain securityFilterChain(
            // DI 자동으로 설정됨, 빌더 패텀처럼 사용한다.
            HttpSecurity http) throws Exception
    {
        http.authorizeHttpRequests(
                // requsetMatchers == 어떤 URL로 오는 요청에 대하여 설정하는지
                // permitAll() == 누가 요청해도 허가한다.
                authHttp -> authHttp.requestMatchers(
                        "/no-auth").permitAll()
        ); // HTTP 요청 허가 관련 설정을 하고 싶다.
        return http.build();
    }
}
