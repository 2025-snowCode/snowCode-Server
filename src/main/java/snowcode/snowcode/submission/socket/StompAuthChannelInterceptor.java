package snowcode.snowcode.submission.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import snowcode.snowcode.auth.service.JwtUtil;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 최초 연결 시에만 확인
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsername(token);

                accessor.setUser(new UsernamePasswordAuthenticationToken(
                        username, null, List.of()
                ));
            }
        }

        // 채팅방 입장
         if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
             log.info("[SUBSCRIBE] 채팅방 입장");
         }

         // 프론트 -> 서버 전송
        if (StompCommand.SEND.equals(accessor.getCommand())) {
            log.info("프론트 -> 서버 응답 수신");
        }

        return message;
    }
}