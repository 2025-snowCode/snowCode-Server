package snowcode.snowcode.chatRoom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.chatRoom.domain.ChatRoom;
import snowcode.snowcode.chatRoom.repository.ChatRoomRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom createChatRoom() {
        return chatRoomRepository.save(ChatRoom.createChatRoom());
    }

    public Optional<ChatRoom> findChatRoomByMembers(Long adminId, Long studentId) {
        return chatRoomRepository.findChatRoomByMembers(adminId, studentId);
    }

    public boolean isPresentChatRoom(Long adminId, Long studentId) {
        return findChatRoomByMembers(adminId, studentId).isPresent();
    }
}
