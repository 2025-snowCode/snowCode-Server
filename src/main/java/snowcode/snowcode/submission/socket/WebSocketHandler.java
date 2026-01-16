package snowcode.snowcode.submission.socket;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import snowcode.snowcode.code.service.CodeExecutionService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ErrorEntity;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.submission.dto.CodeRequestSocket;
import snowcode.snowcode.submission.exception.SubmissionException;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    // 소켓 세션을 저장할 Set
    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final ObjectMapper mapper;
    private final CodeExecutionService codeExecutionService;


    // 소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // TODO Auto-generated method stub
        log.info("{} 연결됨", session.getId());
        sessions.add(session);
//        String token = (String) session.getAttributes().get("token");
        session.sendMessage(new TextMessage("연결 완료"));
    }

    // 소켓 메세지 처리
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
//            log.info("payload {}", payload);

            CodeRequestSocket dto = mapper.readValue(payload, CodeRequestSocket.class);

            // 로직 실행
            String result = codeExecutionService.run(dto.code(), dto.input()).get();

            // 성공 응답
            String jsonResponse = mapper.writeValueAsString(ResponseUtil.success(result));
            session.sendMessage(new TextMessage(jsonResponse));

        } catch (SubmissionException e) {
            log.error("Submission Exception({}) = {}, 코드 실행 실패", e.getCode(), e.getMessage());

            ErrorEntity errorEntity = new ErrorEntity(e.getCode().toString(), e.getMessage());
            BasicResponse<ErrorEntity> errorResponse = ResponseUtil.error(errorEntity);

            String json = mapper.writeValueAsString(errorResponse);
            session.sendMessage(new TextMessage(json));

        } catch (AuthenticationException e) {
            log.warn("Auth error: {}", e.getMessage());
            ErrorEntity errorEntity = new ErrorEntity("인증에 실패하였습니다", e.getMessage());
            BasicResponse<ErrorEntity> errorResponse = ResponseUtil.error(errorEntity);

            String json = mapper.writeValueAsString(errorResponse);
            session.sendMessage(new TextMessage(json));
        } catch (Exception e) {
            log.error("Unexpected error", e);
            ErrorEntity errorEntity = new ErrorEntity("오류가 발생하였습니다.", e.getMessage());
            BasicResponse<ErrorEntity> errorResponse = ResponseUtil.error(errorEntity);

            String json = mapper.writeValueAsString(errorResponse);
            session.sendMessage(new TextMessage(json));
        }
    }


    // 소켓 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
//        session.sendMessage(new TextMessage("WebSocket 연결 종료"));
    }
}
