package snowcode.snowcode.common;


import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;
import snowcode.snowcode.auth.exception.TokenErrorCode;
import snowcode.snowcode.auth.exception.TokenException;
import snowcode.snowcode.auth.service.AuthService;
import snowcode.snowcode.auth.service.JwtUtil;
import jakarta.servlet.http.Cookie;


import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    /**
     * JWT 토큰 검증 필터 수행
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = null;

            // if swagger

            if (isSwaggerRequest(request)) {
                jwt = authWithSwagger(request);
            } else {
                // 쿠키용
                if (request.getCookies()!= null) {
                    jwt = Arrays.stream(request.getCookies())
                            .filter(c -> c.getName().equals("accessToken"))
                            .map(Cookie::getValue)
                            .findFirst()
                            .orElse(null);
                }
            }


            //JWT가 헤더에 있는 경우
            if (jwt != null) {
                String token = jwt.substring(7);
                //JWT 유효성 검증
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.getUsername(token);

                    //유저와 토큰 일치 시 userDetails 생성
                    UserDetails userDetails = authService.loadUserByUsername(username);

                    if (userDetails != null) {
                        //UserDetsils, Password, Role -> 접근권한 인증 Token 생성
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        //현재 Request의 Security Context에 접근권한 설정
//                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
                        if (existingAuth == null || !existingAuth.isAuthenticated()) {
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        }
                    }
                }
                else {
                    SecurityContextHolder.clearContext();
                    authenticationEntryPoint.commence(request, response,
                            new AuthenticationCredentialsNotFoundException("Invalid or expired JWT"));
                    return;
                }
            }

            filterChain.doFilter(request, response); // 다음 필터로 넘기기
        } catch (JwtException | TokenException ex) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response,
                    new TokenException(TokenErrorCode.UNAUTHORIZED));
        }
    }

    private String authWithSwagger(HttpServletRequest request) {
        // jwt용
        return request.getHeader("Authorization");
    }

    private boolean isSwaggerRequest(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return referer != null && referer.contains("/swagger-ui");
    }
}
