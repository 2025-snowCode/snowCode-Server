package snowcode.snowcode.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.chat.domain.Chat;

import java.time.LocalDateTime;

public record ChatListResponse(
        @Schema(description = "메시지 id", example = "1")
        Long messageId,
        @Schema(description = "보낸 회원 id, 해당 필드로 상대인지 구분 가능", example = "1")
        Long memberId,
        @Schema(description = "메시지 타입, CODE or TEXT", example = "TEXT")
        String messageType,
        @Schema(description = "메시지 내용", example = "안녕하세요.")
        String content,
        @Schema(description = "보낸 시각", example = "2026-03-19T13:45:30")
        LocalDateTime sendAt
) {

        public static ChatListResponse of (Chat chat) {
                return new ChatListResponse(chat.getId(), chat.getSenderId(), chat.getType().toString(), chat.getContent(), chat.getCreatedAt());
        }
}