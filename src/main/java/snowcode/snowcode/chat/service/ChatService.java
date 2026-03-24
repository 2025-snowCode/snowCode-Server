package snowcode.snowcode.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.chat.domain.Chat;
import snowcode.snowcode.chat.dto.ChatMessageRequest;
import snowcode.snowcode.chat.repository.ChatRepository;
import snowcode.snowcode.chatRoom.domain.ChatRoom;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;

    @Transactional
    public Chat createChat(ChatMessageRequest message, Long senderId, ChatRoom chatRoom) {
        Chat chat = Chat.createChat(message, senderId, chatRoom);
        chatRoom.updateLastMessage(message.content(), LocalDateTime.now());
        chatRepository.save(chat);
        return chat;
    }

    public List<Chat> findChatListByChatRoomId(Long chatRoomId) {
        return chatRepository.findAllByChatRoomId(chatRoomId);
    }

}
