package snowcode.snowcode.chatRoom.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.service.AuthService;
import snowcode.snowcode.chatRoom.dto.ChatRoomResponse;
import snowcode.snowcode.chatRoom.service.ChatRoomFacade;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
@Tag(name = "채팅방", description = "ChatRoom API")
public class ChatRoomController {

    private final AuthService authService;
    private final ChatRoomFacade chatRoomFacade;

    @GetMapping("/{chatRoomId}")
    @Operation(summary = "채팅방 조회 API", description = "채팅방 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅방 조회에 성공하였습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatRoomResponse.class)
                    )),
            @ApiResponse(responseCode = "400", description = """
                    [UNAUTHORIZED] : 접근 권한이 없습니다. <br>
                    """,
                    content = {@Content(mediaType = "application/json")}),
    })
    public BasicResponse<ChatRoomResponse> findByChatId(@PathVariable Long chatRoomId) {
        Member member = authService.loadMember();
        ChatRoomResponse chatRoomResponse = chatRoomFacade.findChatRoomByChatId(member, chatRoomId);
        return ResponseUtil.success(chatRoomResponse);
    }
}