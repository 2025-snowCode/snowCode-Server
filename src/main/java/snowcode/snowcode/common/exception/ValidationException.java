package snowcode.snowcode.common.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final String message;

    public ValidationException(String message) {
        super();
        this.message = message;
    }
}
