package snowcode.snowcode.submission.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.service.AuthService;
import snowcode.snowcode.code.dto.CodeRequest;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.submission.domain.Submission;
import snowcode.snowcode.submission.dto.SubmissionResponse;
import snowcode.snowcode.submission.service.SubmissionWithCodeFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assignments")
public class SubmissionController {
    private final SubmissionWithCodeFacade submissionWithCodeFacade;
    private final RegistrationService registrationService;
    private final AuthService authService;

    @PostMapping("/{unitId}/{assignmentId}/code")
    @Operation(summary = "코드 제출 API", description = "코드 제출")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코드 제출 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SubmissionResponse.class))}),
    })
    public BasicResponse<SubmissionResponse> createSubmission(@PathVariable Long unitId, @PathVariable Long assignmentId, @RequestBody CodeRequest dto) {
        Member member = authService.loadMember();
        AssignmentRegistration assignmentRegistration = registrationService.findByUnitIdAndAssignmentId(unitId, assignmentId);
        Submission submission = submissionWithCodeFacade.createSubmissionWithCode(member, assignmentRegistration, dto);
        return ResponseUtil.success(SubmissionResponse.of(submission));
    }
}
