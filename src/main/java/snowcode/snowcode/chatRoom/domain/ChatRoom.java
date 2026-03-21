package snowcode.snowcode.chatRoom.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id @Column(name = "chat_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_message")
    private String lastMessage;

    @Column(name = "last_sent_at")
    private LocalDateTime lastSentAt;

    public static ChatRoom createChatRoom() {
        return new ChatRoom();
    }

    // 메시지 추가 시마다 업데이트
    public void updateLastMessage(String lastMessage, LocalDateTime sentAt) {
        this.lastMessage = lastMessage;
        this.lastSentAt = sentAt;
    }

}
