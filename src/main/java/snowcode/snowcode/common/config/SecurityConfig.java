package snowcode.snowcode.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import snowcode.snowcode.auth.service.AuthService;
import snowcode.snowcode.auth.service.JwtUtil;
import snowcode.snowcode.common.JwtAuthFilter;
import snowcode.snowcode.common.exception.CustomAccessDeniedHandler;
import snowcode.snowcode.common.exception.CustomAuthenticationEntryPoint;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Value("${cors.allowed.frontend}")
    private String frontend;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**", "/oauth2/authorization", "/api-docs",
            "/v3/api-docs/**", "/swagger-ui.html", "/oauth2/authorization", "/auth/apple/callback"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthService authService) throws Exception {
        //CSRF, CORS

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));


        //세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        //FormLogin, BasicHttp 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        //JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(new JwtAuthFilter(authService, jwtUtil, authenticationEntryPoint), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );

        http.headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                        .policyDirectives(
                                "default-src 'self'; " +
                                        "script-src 'self' 'unsafe-inline'; " +
                                        "object-src 'none'; " +
                                        "frame-ancestors 'self'; " +
                                        "base-uri 'self'; " +
                                        "form-action 'self'"
                        )
                )
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                .httpStrictTransportSecurity(hsts -> hsts
                        .includeSubDomains(true)
                        .maxAgeInSeconds(31536000))
        );


        // 권한 규칙 작성
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        //@PreAuthrization을 사용할 것이기 때문에 모든 경로에 대한 인증처리는 Pass
//                        .anyRequest().permitAll() // 다 되는 거
                        .anyRequest().authenticated()
        );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(frontend));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH", "OPTIONS"));
//        config.setAllowedHeaders(List.of("*"));
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With"));

//        config.setAllowCredentials(true); // 쿠키 허용
        config.setMaxAge(3600L); // Preflight 요청 캐시 시간 (초)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
