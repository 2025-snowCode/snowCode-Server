package snowcode.snowcode.chatRoom.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatRoomErrorCode {

    NOT_FOUND_CHAT_ROOM("채팅방을 찾을 수 없습니다."),
    CHAT_ROOM_ACCESS_DENIED("채팅방 권한이 존재하지 않습니다.");

    private final String message;
}
