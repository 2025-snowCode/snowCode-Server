package snowcode.snowcode.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode {

    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    INVALID_USER_ROLE("사용자는 관리자 또는 일반 회원이어야 합니다."),
    STUDENT_NOT_FOUND("학생을 찾을 수 없습니다."),
    IS_ALREADY_ENROLLED_STUDENT("이미 등록된 학생입니다."),

    INVALID_ROLE("ADMIN만 가능합니다."),
    INVALID_COURSE_ROLE("강의 생성자만 접근 가능합니다."),
    INVALID_ASSIGNMENT_ROLE("과제 생성자만 접근 가능합니다."),
    INVALID_ENROLLED_ROLE("강의 수강자만 접근 가능합니다."),
    INVALID_CODE_ROLE("코드 제출자/관리자만 접근 가능합니다.");

    private final String message;
}
