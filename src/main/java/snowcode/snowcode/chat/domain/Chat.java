package snowcode.snowcode.chat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.chatRoom.domain.ChatRoom;

import java.time.LocalDateTime;

@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(nullable = false, name = "sender_id")
    private Long senderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatType type;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private Chat(String content, Long senderId, ChatType type, ChatRoom chatRoom) {
        this.content = content;
        this.senderId = senderId;
        this.type = type;
        this.chatRoom = chatRoom;
    }

    public static Chat createChat(String content, Long senderId, String type, ChatRoom chatRoom) {
        return new Chat(content, senderId, ChatType.of(type), chatRoom);
    }
}
