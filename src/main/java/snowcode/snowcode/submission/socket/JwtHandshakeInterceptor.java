package snowcode.snowcode.submission.socket;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import snowcode.snowcode.auth.service.JwtUtil;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {

            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            String token = httpRequest.getParameter("token");

            // 인증 실패
            if (token == null) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            // 유효하지 않은 토큰이면
            if (!jwtUtil.validateToken(token)) {
                return false;
            }

            String username = jwtUtil.getUsername(token);

            attributes.put("username", username);
//            request.getAttributes(); // 여기서 꺼내야 하는 거 아닌지
        }

        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
    }
}