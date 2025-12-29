package snowcode.snowcode.auth.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class TokenException extends AuthenticationException {
    private TokenErrorCode code;
    private String message;

    public TokenException(TokenErrorCode code) {
        super(code.getMessage());
        this.code = code;
        this.message = code.getMessage();
    }

    public TokenException(TokenErrorCode code, String message) {
        super(code.getMessage());
        this.code = code;
        this.message = message;
    }
}
