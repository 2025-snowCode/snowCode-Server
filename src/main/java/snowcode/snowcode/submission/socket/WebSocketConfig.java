package snowcode.snowcode.submission.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    @Value("${cors.allowed.frontend}")
    private String frontend;

    private final WebSocketHandler webSocketChatHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketChatHandler, "/ws/conn")
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOriginPatterns(frontend);
    }
}