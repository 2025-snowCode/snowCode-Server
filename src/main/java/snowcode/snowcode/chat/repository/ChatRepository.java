package snowcode.snowcode.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.chat.domain.Chat;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findAllByChatRoomId(Long chatRoomId);
}
