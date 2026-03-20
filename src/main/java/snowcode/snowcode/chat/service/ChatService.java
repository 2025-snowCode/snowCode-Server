package snowcode.snowcode.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.chat.domain.Chat;
import snowcode.snowcode.chat.repository.ChatRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;

    public List<Chat> findChatListByChatRoomId(Long chatRoomId) {
        return chatRepository.findAllByChatRoomId(chatRoomId);
    }

}
