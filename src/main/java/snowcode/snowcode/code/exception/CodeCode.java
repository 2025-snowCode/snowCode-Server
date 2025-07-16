package snowcode.snowcode.code.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeCode {

    CODE_NOT_FOUND("코드를 찾을 수 없습니다."),
    INVALID_LANGUAGE_TYPE("지원하지 않는 언어입니다.");

    private final String message;
}
