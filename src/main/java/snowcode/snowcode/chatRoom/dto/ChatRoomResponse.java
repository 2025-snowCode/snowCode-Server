package snowcode.snowcode.chatRoom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.chat.dto.ChatListResponse;
import snowcode.snowcode.chatRoom.domain.ChatRoom;

import java.util.List;

public record ChatRoomResponse(
        @Schema(description = "채팅방 id", example = "1")
        Long chatRoomId,
        @Schema(description = "상대 회원 id", example = "2")
        Long opponentId,
        @Schema(description = "상대가 학생일 경우 학번 그렇지 않은 경우(교수) null", example = "2313398")
        String opponentStudentId,
        @Schema(description = "상대의 이름", example = "주아정")
        String opponentName,
        @Schema(description = "메시지 개수", example = "1")
        int size,
        @Schema(description = "메시지 list")
        List<ChatListResponse> messages
) {

        public static ChatRoomResponse of (Member opponent, ChatRoom chatRoom, List<ChatListResponse> messages) {
                return new ChatRoomResponse(chatRoom.getId(), opponent.getId(), opponent.getStudentId(), opponent.getName(), messages.size(), messages);
        }
}