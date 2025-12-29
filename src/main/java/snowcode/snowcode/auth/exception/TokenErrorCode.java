package snowcode.snowcode.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenErrorCode {

    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    INVALID_PROVIDER("provider은 KAKAO or GOOGLE이어야 합니다."),
    TOKEN_HAS_EXPIRED("토큰의 유효 기간이 만료되었습니다."),
    NOT_FOUND_TOKEN("토큰을 찾을 수 없습니다."),
    NOT_FOUND_USERNAME("회원의 Username이 유효하지 않습니다."),
    UNAUTHORIZED("접근 권한이 없습니다."),
    PROVIDER_MISMATCH("provider이 일치하지 않습니다.");

    private final String message;
}
