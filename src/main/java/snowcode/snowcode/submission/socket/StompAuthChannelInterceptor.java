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
            log.info("CONNECT user BEFORE = {}", accessor.getUser());

            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null) {
                token = token.substring(7);
            }
            try {
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.getUsername(token);

                    accessor.setUser(new UsernamePasswordAuthenticationToken(
                            username, null, List.of()
                    ));
                    log.info("CONNECT user AFTER = {}", accessor.getUser());
                }
            } catch (Exception e) {
                log.error("토큰 검증 중 에러 발생");
            }
        }

        // 채팅방 입장
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            log.info("[SUBSCRIBE] 채팅방 입장");
        }

        // 프론트 -> 서버 전송
        if (StompCommand.SEND.equals(accessor.getCommand())) {
            log.info("프론트 -> 서버 응답 수신");
            log.info("SEND user BEFORE = {}", accessor.getUser());
            if (accessor.getUser() == null) {
                String token = accessor.getFirstNativeHeader("Authorization");

                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);

                    if (jwtUtil.validateToken(token)) {
                        String username = jwtUtil.getUsername(token);

                        accessor.setUser(new UsernamePasswordAuthenticationToken(
                                username, null, List.of()
                        ));
                        log.info("SEND user AFTER = {}", accessor.getUser());
                    }
                }
            }

        }

        return message;
    }
}