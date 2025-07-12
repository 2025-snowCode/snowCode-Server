package snowcode.snowcode.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode {

    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    INVALID_USER_ROLE("사용자는 관리자 또는 일반 회원이어야 합니다.");

    private final String message;
}
