package snowcode.snowcode.chat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatErrorCode {

    INVALID_CHAT_TYPE("채팅의 타입이 일치하지 않습니다.");

    private final String message;
}
