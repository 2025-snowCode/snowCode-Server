package snowcode.snowcode.chat.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class ChatException extends AuthenticationException {
    private ChatErrorCode code;
    private String message;

    public ChatException(ChatErrorCode code) {
        super(code.getMessage());
        this.code = code;
        this.message = code.getMessage();
    }

    public ChatException(ChatErrorCode code, String message) {
        super(code.getMessage());
        this.code = code;
        this.message = message;
    }
}
