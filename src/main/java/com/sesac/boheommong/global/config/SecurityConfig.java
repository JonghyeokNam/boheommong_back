package com.sesac.boheommong.global.config;

import com.sesac.boheommong.domain.user.repository.UserRepository;
import com.sesac.boheommong.domain.user.service.UserService;
import com.sesac.boheommong.global.jwt.filter.TokenAuthenticationFilter;
import com.sesac.boheommong.global.jwt.repository.RefreshTokenRepository;
import com.sesac.boheommong.global.jwt.service.TokenProvider;
import com.sesac.boheommong.global.oauth2.handler.OAuth2SuccessHandler;
import com.sesac.boheommong.global.oauth2.service.CustomOAuth2UserService;
import com.sesac.boheommong.global.oauth2.util.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.sesac.boheommong.global.totp.service.TOTPService; // 추가
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final TOTPService totpService;  // 주입 (TOTPService)
    private final UserRepository userRepository;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 1) CORS
                .cors(cors -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.addAllowedOrigin("http://localhost:3000");
                    configuration.addAllowedOrigin("http://boheommong.site");
                    configuration.addAllowedMethod("*");
                    configuration.addAllowedHeader("*");
                    configuration.setAllowCredentials(true);
                    cors.configurationSource(request -> configuration);
                })
                // 2) CSRF / HTTP Basic / Form Login 등 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                // 3) 세션 정책
                //    “TOTP” 위해 세션 사용 -> IF_REQUIRED (로그인/인증 과정에서만 만듦)
                .sessionManagement(management ->
                        management.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                // 4) 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 예: TOTP 관련 API는 인증 없이 접근 가능하거나, 조건에 따라 달리
                        // .requestMatchers(new AntPathRequestMatcher("/api/totp/**")).permitAll()
                        .anyRequest().permitAll()
                )

                // 5) OAuth2 설정
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorizationEndpoint ->
                                authorizationEndpoint.authorizationRequestRepository(
                                        oAuth2AuthorizationRequestBasedOnCookieRepository()
                                )
                        )
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.userService(customOAuth2UserService))
                        // 기존에 있던 oAuth2SuccessHandler를 수정해, TOTP를 강제
                        // (새로 만든 onAuthenticationSuccess() 구현)
                        .successHandler(oAuth2SuccessHandler())
                )

                // 6) 예외 처리
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**")
                        )
                )

                // 7) JWT 인증 필터
                .addFilterBefore(tokenAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * 기존에 OAuth2SuccessHandler가 즉시 JWT 발급하던 로직 제거
     * -> TOTPService 주입, "QR or OTP" 로 redirect만 수행
     */
    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        // 새 생성자를 만들어서, TokenProvider 등 제거하거나
        // (단, 만약 안 쓰면 주석/안 받아도 됨)
        return new OAuth2SuccessHandler(userService, totpService, userRepository);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
