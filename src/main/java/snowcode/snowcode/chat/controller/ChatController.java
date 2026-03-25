package snowcode.snowcode.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.service.AuthService;
import snowcode.snowcode.chat.dto.ChatMessageRequest;
import snowcode.snowcode.chat.service.ChatFacade;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final AuthService authService;
    private final ChatFacade chatFacade;

    @MessageMapping("/chat")
    public void sendPrivateMessage(ChatMessageRequest message, Principal principal) {

        log.info("sendMessage 수신");

        // sender 설정
        String username = principal.getName();
        Member sender = authService.findByUsername(username);

        chatFacade.sendAndSaveMessage(message, sender);

        log.info("브로드캐스트 성공");
    }
}