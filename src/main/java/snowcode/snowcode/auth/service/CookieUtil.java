package snowcode.snowcode.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CookieUtil {

    @Value("${cors.allowed.cookie}")
    private String domain;

    public ResponseCookie createAccessCookie(String token) {
        return ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Lax")
                .domain(domain)
                .maxAge(60 * 60 * 24) // 하루
                .build();
    }

    public ResponseCookie createRefreshCookie(String token) {
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/oauth2/authorization/auth/reissue")
                .sameSite("Lax")
                .domain(domain)
                .maxAge(60 * 60 * 24 * 14) // 2주
                .build();
    }
}
