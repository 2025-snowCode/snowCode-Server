package snowcode.snowcode.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.chat.domain.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
