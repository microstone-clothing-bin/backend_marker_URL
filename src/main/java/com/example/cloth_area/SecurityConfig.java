package com.example.cloth_area;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// 실행 시 로그인 페이지 없애는 클래스 파일임

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/h2-console/**").permitAll()  // H2 콘솔 허용
                        .anyRequest().permitAll()  // 모든 요청 허용
                )
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화 (POST, PUT, DELETE 요청시 에러 방지)
                .headers(headers -> headers.frameOptions().disable())  // iframe 허용 (H2 콘솔용)
                .formLogin(form -> form.disable());  // 로그인 폼 비활성화 (로그인 화면 안 뜨게)

        return http.build();
    }
}