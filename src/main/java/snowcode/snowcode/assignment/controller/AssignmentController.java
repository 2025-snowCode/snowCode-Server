package snowcode.snowcode.assignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentDeleteFacade assignmentDeleteFacade;
    private final RegistrationScheduleService registrationScheduleService;
    private final AssignmentWithTestcaseFacade assignmentWithTestcaseFacade;

    @PostMapping
    @Operation(summary = "과제 추가 API", description = "과제 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "과제 추가 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AssignmentInfoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_INPUT",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<AssignmentInfoResponse> createAssignment(@Valid @RequestBody AssignmentCreateWithTestcaseRequest dto) {
        AssignmentInfoResponse assignment = assignmentWithTestcaseFacade.createAssignment(dto);
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
        AssignmentInfoResponse assignmentInfo = assignmentWithTestcaseFacade.findAssignmentInfo(assignmentId);
        return ResponseUtil.success(assignmentInfo);
    }

    @GetMapping("/{memberId}/schedule")
    @Operation(summary = "일정 리스트업 API", description = "해야 할 과제 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일정 리스트업 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationScheduleResponse.class))}),
    })
    public BasicResponse<RegistrationScheduleResponse> listUpMySchedule(@PathVariable Long memberId) {
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
        AssignmentInfoResponse assignment = assignmentWithTestcaseFacade.updateAssignment(assignmentId, dto);
        return ResponseUtil.success(assignment);
    }

    @DeleteMapping("/{assignmentId}")
    @Operation(summary = "과제 삭제 API", description = "과제 삭제 (관련 연결된 과제, 제출 코드 등 모두 삭제)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "과제 삭제 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "과제가 존재하지 않습니다",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<String> deleteAssignment(@PathVariable Long assignmentId) {
        assignmentDeleteFacade.deleteAssignmentWithAll(assignmentId);
        return ResponseUtil.success("과제 삭제에 성공하였습니다.");
    }
}
