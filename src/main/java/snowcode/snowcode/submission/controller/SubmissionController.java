package snowcode.snowcode.submission.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.service.AuthService;
import snowcode.snowcode.code.dto.CodeRequest;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.submission.dto.SubmissionResponse;
import snowcode.snowcode.submission.service.SubmissionWithCodeFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assignments")
@Tag(name = "코드 제출", description = "Submission API")
public class SubmissionController {
    private final SubmissionWithCodeFacade submissionWithCodeFacade;
    private final RegistrationService registrationService;
    private final AuthService authService;

    @PostMapping("/{unitId}/{assignmentId}/code")
    @Operation(summary = "코드 제출 API", description = """
            코드 제출 시 code에는 반드시 파싱 가능하도록 보내주세요! \n
            ex) print("hello") (X), print(\\\\\\"hello\\\\\\") (O)
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코드 제출 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SubmissionResponse.class))}),
    })
    public BasicResponse<SubmissionResponse> createSubmission(@PathVariable Long unitId, @PathVariable Long assignmentId, @RequestBody CodeRequest dto) {
        Member member = authService.loadMember();
        AssignmentRegistration assignmentRegistration = registrationService.findByUnitIdAndAssignmentId(unitId, assignmentId);
        SubmissionResponse submission = submissionWithCodeFacade.createSubmissionWithCode(member, assignmentRegistration, dto);
        return ResponseUtil.success(submission);
    }

    @PostMapping("/execute")
    @Operation(summary = "코드 실행 API", description = """
            ### WebSocket Endpoint \n
            - wss://{BackURL}/ws/conn \n
            
            \n
            
            ### 중요!
            - 사용 이후에는 반드시 끊어주세요!
            - backURL에 포트번호는 포함되지 않습니다.
            
            ### 인증
            - WebSocket 요청 헤더에 JWT를 포함해야 합니다: Authorization: Bearer {JWT_TOKEN}
           
            \n
           
            ### 요청(JSON 예시)
            
            - ** input 필수! 만약 input이 필요 없는 문제라면 빈 값이라도 보내주세요! ** \n
            
            \n
            
            ### input 없는 버전 (Empty String)
            - {
                "input": "",
                "code": "print(\\"hello\\")"
            }
            
            \n
            
            ### input 있는 버전
            
            - {
                "input": "1 2",
                "code": "a, b = map(int, input().split()) \\nprint(a + b)"
            }
            
            \n
            
            ### 200 - 정상 응답(JSON)
            
            - {
                "success": true,
                "response": "hello"
            }
            
            \n
            
            
            ### 테스트 방법 (Swagger 테스트 불가, 포스트맨으로 테스트)
        
            - Postman → WebSocket 탭 -> \n
            - wss://{backURL}/ws/conn (https용) \n
            - Header -> Authorization: Bearer {JWT}  \n
            - Body(JSON) -> 위 예시 입력 \n
            - 블로그 참조 : https://senslife.tistory.com/52 \n
            \n
            cf) 연결 지속 시간은 1h으로 설정해두었습니다. \n
            
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코드 실행 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SubmissionResponse.class))}),
    })
    public BasicResponse<String> executeCode() {
        return ResponseUtil.success("Swagger 문서에서 WebSocket 연결 방법 참고");
    }
}
