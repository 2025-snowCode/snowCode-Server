package snowcode.snowcode.chatRoomUser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.chatRoom.domain.ChatRoom;
import snowcode.snowcode.chatRoomUser.domain.ChatRoomUser;
import snowcode.snowcode.chatRoomUser.repository.ChatRoomUserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomUserService {

    private final ChatRoomUserRepository chatRoomUserRepository;

    @Transactional
    public void createChatRoomUser(Member admin, Member user, ChatRoom chatRoom) {
        ChatRoomUser chatRoomUserByAdmin = ChatRoomUser.createChatRoomUser(admin, chatRoom);
        ChatRoomUser chatRoomUserByUser = ChatRoomUser.createChatRoomUser(user, chatRoom);
        chatRoomUserRepository.saveAll(List.of(chatRoomUserByAdmin, chatRoomUserByUser));
    }

    public List<ChatRoomUser> findChatRoomUserByChatRoomId(Long chatRoomId) {
        return chatRoomUserRepository.findByChatRoomId(chatRoomId);
    }

}
