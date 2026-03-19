package snowcode.snowcode.chatRoom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.chatRoom.domain.ChatRoom;
import snowcode.snowcode.chatRoomUser.service.ChatRoomUserService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomFacade {

    private final ChatRoomService chatRoomService;
    private final ChatRoomUserService chatRoomUserService;

    @Transactional
    public void createChatRoom(Member admin, Member user) {
        // 채팅방 만들기
        ChatRoom chatRoom = chatRoomService.createChatRoom();
        // 채팅 참여자 추가
        chatRoomUserService.createChatRoomUser(admin, user, chatRoom);
    }
}
