package snowcode.snowcode.chatRoom.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class ChatRoomException extends AuthenticationException {
    private ChatRoomErrorCode code;
    private String message;

    public ChatRoomException(ChatRoomErrorCode code) {
        super(code.getMessage());
        this.code = code;
        this.message = code.getMessage();
    }

    public ChatRoomException(ChatRoomErrorCode code, String message) {
        super(code.getMessage());
        this.code = code;
        this.message = message;
    }
}
