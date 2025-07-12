package snowcode.snowcode.auth.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    private AuthErrorCode code;
    private String message;

    public AuthException(AuthErrorCode code) {
        super();
        this.code = code;
        this.message = code.getMessage();
    }

    public AuthException(AuthErrorCode code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
