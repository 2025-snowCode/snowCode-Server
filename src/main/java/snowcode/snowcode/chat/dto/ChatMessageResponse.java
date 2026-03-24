package snowcode.snowcode.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.chat.domain.Chat;
import snowcode.snowcode.chatRoom.domain.ChatRoom;

import java.time.LocalDateTime;

public record ChatMessageResponse (

        @Schema(description = "채팅 타입, TEXT or CODE", example = "TEXT")
        String type,
        @Schema(description = "채팅방 id", example = "1")
        Long chatRoomId,
        @Schema(description = "보낸 사람의 id", example = "1")
        Long senderId,
        @Schema(description = "메시지 내용", example = "안녕하세요.")
        String content,
        @Schema(description = "발송 시각", example = "2026-03-20T14:32:10.123")
        LocalDateTime sendAt
) {

    public static ChatMessageResponse of (ChatRoom chatRoom, Chat chat) {
        return new ChatMessageResponse(chat.getType().toString(), chatRoom.getId(), chat.getSenderId(), chat.getContent(), chat.getSendAt());
    }
}