package snowcode.snowcode.chatRoom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.chatRoom.domain.ChatRoom;

import java.time.LocalDateTime;

public record ChatRoomListResponse(
        @Schema(description = "채팅방 id", example = "1")
        Long chatRoomId,
        @Schema(description = "상대 회원 id", example = "1")
        Long opponentMemberId,
        @Schema(description = "학생인 경우 상대 학번, 교수인 경우 null", example = "2313398")
        String studentId,
        @Schema(description = "상대 이름", example = "주아정")
        String name,
        @Schema(description = "마지막 발송 메시지", example = "안녕하세요")
        String lastMessage,
        @Schema(description = "마지막 발송 시각", example = "2026-03-20T14:32:10.123")
        LocalDateTime lastSentAt
) {

    public static ChatRoomListResponse of (ChatRoom chatRoom, Member opponent) {
        return new ChatRoomListResponse(chatRoom.getId(), opponent.getId(), opponent.getStudentId(), opponent.getName(), chatRoom.getLastMessage(), chatRoom.getLastSentAt());
    }
}