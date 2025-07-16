package snowcode.snowcode.enrollment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnrollmentErrorCode {

    ENROLLMENT_NOT_FOUND("수강 정보가 존재하지 않습니다.");

    private final String message;
}
