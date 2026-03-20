package snowcode.snowcode.chatRoomUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.chatRoomUser.domain.ChatRoomUser;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    List<ChatRoomUser> findByChatRoomId(Long chatRoomId);
}
