package snowcode.snowcode.chatRoomUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.chatRoomUser.domain.ChatRoomUser;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
}
