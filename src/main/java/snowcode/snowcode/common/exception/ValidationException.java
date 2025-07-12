package snowcode.snowcode.common.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException { // TODO - STUDY
    private final String message;

    public ValidationException() {
        super();
        this.message = "값이 유효하지 않습니다.";
    }
}