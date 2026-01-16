package snowcode.snowcode.assignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.assignment.dto.AssignmentCreateWithTestcaseRequest;
import snowcode.snowcode.assignment.dto.AssignmentInfoResponse;
import snowcode.snowcode.assignment.dto.AssignmentUpdateWithTestcaseRequest;
import snowcode.snowcode.assignment.service.AssignmentDeleteFacade;
import snowcode.snowcode.assignment.service.AssignmentWithTestcaseFacade;
import snowcode.snowcode.assignmentRegistration.dto.RegistrationScheduleResponse;
import snowcode.snowcode.assignmentRegistration.service.RegistrationScheduleService;
import snowcode.snowcode.auth.service.AuthContext;
import snowcode.snowcode.auth.service.AuthService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assignments")
@Tag(name = "과제", description = "Assignment API")
public class AssignmentController {

    private final AssignmentDeleteFacade assignmentDeleteFacade;
    private final RegistrationScheduleService registrationScheduleService;
    private final AssignmentWithTestcaseFacade assignmentWithTestcaseFacade;
    private final AuthService authService;
    private final AuthContext authContext;

    @PostMapping
    @Operation(summary = "과제 추가 API", description = "과제 추가, testcase(input) 존재하지 않을 시 null이 아닌 빈 값(\"\")으로 보내주세요!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "과제 추가 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AssignmentInfoResponse.class))}),
    })
    public BasicResponse<AssignmentInfoResponse> createAssignment(@Valid @RequestBody AssignmentCreateWithTestcaseRequest dto) {
        // 인가
        authContext.isAdmin();
        Long memberId = authService.loadMember().getId();
        AssignmentInfoResponse assignment = assignmentWithTestcaseFacade.createAssignment(memberId, dto);
        return ResponseUtil.success(assignment);
    }

    @GetMapping("/{assignmentId}")
    @Operation(summary = "과제 조회 API", description = "과제 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "과제 조회 성공 (코드 화면에서 과제 조회)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AssignmentInfoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "과제가 존재하지 않습니다",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<AssignmentInfoResponse> getDetailAssignment(@PathVariable Long assignmentId) {
        authContext.isAssignmentOwner(assignmentId); // 인가
        AssignmentInfoResponse assignmentInfo = assignmentWithTestcaseFacade.findAssignmentInfo(assignmentId);
        return ResponseUtil.success(assignmentInfo);
    }

    @GetMapping("/schedule")
    @Operation(summary = "일정 리스트업 API", description = "해야 할 과제 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일정 리스트업 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationScheduleResponse.class))}),
    })
    public BasicResponse<RegistrationScheduleResponse> listUpMySchedule() {
        Long memberId = authService.loadMember().getId();
        RegistrationScheduleResponse assignments = registrationScheduleService.listUpMySchedule(memberId);
        return ResponseUtil.success(assignments);
    }

    @PutMapping("/{assignmentId}")
    @Operation(summary = "과제 수정 API", description = "과제 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "과제 수정 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AssignmentInfoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_INPUT",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "404", description = "과제가 존재하지 않습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<AssignmentInfoResponse> updateAssignment(@PathVariable Long assignmentId, @Valid @RequestBody AssignmentUpdateWithTestcaseRequest dto) {
        authContext.isAssignmentOwner(assignmentId); // 인가
        AssignmentInfoResponse assignment = assignmentWithTestcaseFacade.updateAssignment(assignmentId, dto);
        return ResponseUtil.success(assignment);
    }

    @DeleteMapping("/{assignmentId}")
    @Operation(summary = "과제 삭제 API", description = "과제 삭제 (관련 연결된 과제, 제출 코드 등 모두 삭제)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "과제 삭제 성공",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "과제가 존재하지 않습니다",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<String> deleteAssignment(@PathVariable Long assignmentId) {
        authContext.isAssignmentOwner(assignmentId); // 인가
        assignmentDeleteFacade.deleteAssignmentWithAll(assignmentId);
        return ResponseUtil.success("과제 삭제에 성공하였습니다.");
    }
}
