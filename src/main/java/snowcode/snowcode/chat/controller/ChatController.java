package snowcode.snowcode.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.service.AuthService;
import snowcode.snowcode.chat.dto.ChatMessageRequest;
import snowcode.snowcode.chat.service.ChatFacade;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final AuthService authService;
    private final ChatFacade chatFacade;

    @MessageMapping("/chat")
    public void sendPrivateMessage(ChatMessageRequest message) {

        // sender 설정
        Member sender = authService.loadMember();

        chatFacade.sendAndSaveMessage(message, sender);
    }
}