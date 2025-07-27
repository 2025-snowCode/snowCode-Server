package snowcode.snowcode.assignment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.assignment.dto.*;
import snowcode.snowcode.assignment.service.AssignmentDeleteFacade;
import snowcode.snowcode.assignmentRegistration.dto.RegistrationScheduleResponse;
import snowcode.snowcode.assignmentRegistration.service.RegistrationScheduleService;
import snowcode.snowcode.assignment.service.AssignmentService;
import snowcode.snowcode.assignment.service.AssignmentWithTestcaseFacade;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AssignmentDeleteFacade assignmentDeleteFacade;
    private final RegistrationScheduleService registrationScheduleService;
    private final AssignmentWithTestcaseFacade assignmentWithTestcaseFacade;

    @PostMapping
    public BasicResponse<AssignmentInfoResponse> createAssignment(@Valid @RequestBody AssignmentCreateWithTestcaseRequest dto) {
        AssignmentInfoResponse assignment = assignmentWithTestcaseFacade.createAssignment(dto);
        return ResponseUtil.success(assignment);
    }

    @GetMapping("/{assignmentId}")
    public BasicResponse<AssignmentInfoResponse> getDetailAssignment(@PathVariable Long assignmentId) {
        AssignmentInfoResponse assignmentInfo = assignmentWithTestcaseFacade.findAssignmentInfo(assignmentId);
        return ResponseUtil.success(assignmentInfo);
    }

    @GetMapping
    public BasicResponse<AssignmentCountListResponse> findAllAssignment() {
        AssignmentCountListResponse assignments = assignmentService.findAllAssignment();
        return ResponseUtil.success(assignments);
    }

    @GetMapping("/{memberId}/schedule")
    public BasicResponse<RegistrationScheduleResponse> listUpMySchedule(@PathVariable Long memberId) {
        RegistrationScheduleResponse assignments = registrationScheduleService.listUpMySchedule(memberId);
        return ResponseUtil.success(assignments);
    }

    @PutMapping("/{assignmentId}")
    public BasicResponse<AssignmentInfoResponse> updateAssignment(@PathVariable Long assignmentId, @Valid @RequestBody AssignmentUpdateWithTestcaseRequest dto) {
        AssignmentInfoResponse assignment = assignmentWithTestcaseFacade.updateAssignment(assignmentId, dto);
        return ResponseUtil.success(assignment);
    }

    @DeleteMapping("/{assignmentId}")
    public BasicResponse<String> deleteAssignment(@PathVariable Long assignmentId) {
        assignmentDeleteFacade.deleteAssignmentWithAll(assignmentId);
        return ResponseUtil.success("과제 삭제에 성공하였습니다.");
    }
}
