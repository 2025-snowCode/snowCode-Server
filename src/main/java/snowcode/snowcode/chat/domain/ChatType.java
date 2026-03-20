package snowcode.snowcode.chat.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import snowcode.snowcode.chat.exception.ChatErrorCode;
import snowcode.snowcode.chat.exception.ChatException;

import java.util.Arrays;

@Getter
public enum ChatType {
    TEXT, CODE;

    public static ChatType of(@NotNull String type) {
        return Arrays.stream(ChatType.values())
                .filter(r -> r.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new ChatException(ChatErrorCode.INVALID_CHAT_TYPE));
    }

}
