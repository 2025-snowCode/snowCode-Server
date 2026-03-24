package snowcode.snowcode.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.service.MemberService;
import snowcode.snowcode.chat.domain.Chat;
import snowcode.snowcode.chat.dto.ChatMessageRequest;
import snowcode.snowcode.chat.dto.ChatMessageResponse;
import snowcode.snowcode.chatRoom.domain.ChatRoom;
import snowcode.snowcode.chatRoom.exception.ChatRoomErrorCode;
import snowcode.snowcode.chatRoom.exception.ChatRoomException;
import snowcode.snowcode.chatRoom.service.ChatRoomFacade;
import snowcode.snowcode.chatRoom.service.ChatRoomService;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatFacade {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;
    private final ChatRoomFacade chatRoomFacade;
    private final ChatService chatService;

    @Transactional
    public void sendAndSaveMessage(ChatMessageRequest message, Member sender) {
        ChatRoom chatRoom = chatRoomService.findByChatRoomId(message.chatRoomId());
        Member receiver = memberService.findMember(message.receiverId());

        // 인가
        if (!chatRoomFacade.hasUser(chatRoom, sender.getId())) {
            throw new ChatRoomException(ChatRoomErrorCode.CHAT_ROOM_ACCESS_DENIED);
        }

        Chat chat = chatService.createChat(message, sender.getId(), chatRoom);
        ChatMessageResponse response = ChatMessageResponse.of(chatRoom, chat);

        // sender에게 전송
        messagingTemplate.convertAndSendToUser(
                sender.getUsername().toString(),
                "/queue/messages",
                response
        );

        // receiver에게 전송
        messagingTemplate.convertAndSendToUser(
                receiver.getUsername().toString(),
                "/queue/messages",
                response
        );
    }
}
