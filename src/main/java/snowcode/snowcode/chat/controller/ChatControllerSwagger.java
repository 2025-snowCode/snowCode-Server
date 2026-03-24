package snowcode.snowcode.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snowcode.snowcode.chat.dto.ChatMessageRequest;
import snowcode.snowcode.chat.dto.ChatMessageResponse;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ws/stomp")
@Tag(name = "채팅", description = "Chat API")
public class ChatControllerSwagger {

    @GetMapping
    @Operation(summary = "채팅 전송 API", description = """
            ### WebSocket Endpoint (연결용) \n
            - wss://{BackURL}/ws/stomp \n
            
            \n
            
            ### 중요!
            - 사용 이후에는 반드시 끊어주세요!
            - backURL에 포트번호는 포함되지 않습니다.
            
            ### 인증
            - WebSocket 요청 헤더에 JWT를 포함해야 합니다: Authorization: Bearer {JWT_TOKEN}
           
            \n
           
            ### 메시지 보내기 (클라이언트 -> 서버)
            
            - /pub/chat
            - ChatMessageRequest
            {
            	"type": "TEXT",
            	"chatRoomId": 1,
            	"receiverId": 1,
                "content": "질문 있습니다.."
            }
            
            \n
            
            ### 응답
            - ChatMessageResponse
            {
            	"type": "TEXT",
            	"chatRoomId": 1,
            	"senderId": 1,
                "content": "질문 있습니다..",
                "sendAt": "2026-03-20T14:32:10.123"
            }
            
            \n
            
            ### 클라이언트 구독
            - /user/queue/messages
            
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅 전송에 성공하였습니다.",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ChatMessageResponse.class))}),
    })
    public BasicResponse<String> findCode(ChatMessageRequest dto) {
        return ResponseUtil.success("성공");
    }
}
