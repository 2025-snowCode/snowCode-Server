package snowcode.snowcode.chatRoomUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import snowcode.snowcode.chatRoomUser.domain.ChatRoomUser;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    List<ChatRoomUser> findByChatRoomId(Long chatRoomId);

    @Query("""
        SELECT cru
        FROM ChatRoomUser cru
        JOIN FETCH cru.chatRoom
        WHERE cru.member.id = :memberId
    """)
    List<ChatRoomUser> findByMemberId(Long memberId);

    @Query("""
        SELECT cru
        FROM ChatRoomUser cru
        JOIN FETCH cru.chatRoom
        JOIN FETCH cru.member
        WHERE cru.chatRoom.id IN :chatRoomIds
    """)

    List<ChatRoomUser> findAllByChatRoomIdIn(List<Long> chatRoomIds);
}
