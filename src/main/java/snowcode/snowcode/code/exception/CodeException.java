package snowcode.snowcode.code.exception;

import lombok.Getter;

@Getter
public class CodeException extends RuntimeException {
    private CodeCode code;
    private String message;

    public CodeException(CodeCode code) {
        super();
        this.code = code;
        this.message = code.getMessage();
    }

    public CodeException(CodeCode code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
