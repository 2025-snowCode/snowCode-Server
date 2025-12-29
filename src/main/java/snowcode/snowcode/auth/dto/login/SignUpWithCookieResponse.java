package snowcode.snowcode.auth.dto.login;

import lombok.Builder;
import org.springframework.http.ResponseCookie;
import snowcode.snowcode.auth.domain.Member;

@Builder
public record SignUpWithCookieResponse(
        // 내부 매핑용
        Member member,
        ResponseCookie accessCookie,
        ResponseCookie refreshCookie,
        String accessToken


) {

    public static SignUpWithCookieResponse of(Member member, ResponseCookie accessCookie, ResponseCookie refreshCookie, String accessToken) {
        return new SignUpWithCookieResponse(member, accessCookie, refreshCookie, accessToken);
    }
}
