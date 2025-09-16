package com.example.cloth_area;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

// 실행 시 로그인 페이지 없애는 클래스 파일임

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/h2-console/**").permitAll()  // H2 콘솔 허용
                        .anyRequest().permitAll()  // 그 외 모든 요청 허용
                )
                // csrf 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // headers 설정 수정: frameOptions를 비활성화하는 새로운 방식
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )

                // formLogin 비활성화
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}