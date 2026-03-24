package snowcode.snowcode.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageRequest (

        @NotBlank @Schema(description = "채팅 타입, TEXT or CODE", example = "TEXT")
        String type,
        @NotNull @Schema(description = "채팅방 id", example = "1")
        Long chatRoomId,
        @NotNull @Schema(description = "받는 사람의 id", example = "1")
        Long receiverId,
        @NotBlank @Schema(description = "메시지 내용", example = "안녕하세요.")
        String content
) {
}
