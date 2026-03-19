package snowcode.snowcode.chatRoom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import snowcode.snowcode.chatRoom.domain.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("""
        SELECT cr
        FROM ChatRoom cr
        WHERE EXISTS (
            SELECT 1
            FROM ChatRoomUser u
            WHERE cr = u.chatRoom AND u.member.id = :adminId
        ) AND EXISTS (
            SELECT 1
            FROM ChatRoomUser u2
            WHERE cr = u2.chatRoom AND u2.member.id = :studentId
        )
    """)
    Optional<ChatRoom> findChatRoomByMembers(Long adminId, Long studentId);

}
