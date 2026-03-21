package snowcode.snowcode.chatRoom.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ChatRoomListResponseWithCount(
        @Schema(description = "채팅방 수", example = "1")
        int count,
        @Schema(description = "채팅방 목록 리스트")
        List<ChatRoomListResponse> chatRoomList) {

    public static ChatRoomListResponseWithCount of(List<ChatRoomListResponse> chatRoomList) {
        return new ChatRoomListResponseWithCount(chatRoomList.size(), chatRoomList);
    }
}