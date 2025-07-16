package snowcode.snowcode.testcase.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TestcaseErrorCode {

    TESTCASE_NOT_FOUND("테스트케이스가 존재하지 않습니다."),
    INVALID_TESTCASE_ROLE_TYPE("TESTCASE 또는 EXAMPLE만 입력 가능합니다.");

    private final String message;
}
