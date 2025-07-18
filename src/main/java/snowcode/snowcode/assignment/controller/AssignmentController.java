package snowcode.snowcode.assignment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.assignment.dto.AssignmentCountListResponse;
import snowcode.snowcode.assignment.dto.AssignmentRequest;
import snowcode.snowcode.assignment.dto.AssignmentResponse;
import snowcode.snowcode.assignment.service.AssignmentRegistrationFacade;
import snowcode.snowcode.assignment.service.AssignmentService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.unit.domain.Unit;
import snowcode.snowcode.unit.service.UnitService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AssignmentRegistrationFacade assignmentRegistrationFacade;
    private final UnitService unitService;

    @PostMapping("{unitId}")
    public BasicResponse<AssignmentResponse> createAssignment(@PathVariable Long unitId, @Valid @RequestBody AssignmentRequest dto) {
        Unit unit = unitService.findUnit(unitId);
        AssignmentResponse assignment = assignmentService.createAssignment(unit, dto);
        return ResponseUtil.success(assignment);
    }

    @GetMapping("/{assignmentId}")
    public BasicResponse<AssignmentResponse> findAssignment(@PathVariable Long assignmentId) {
        AssignmentResponse assignment = assignmentService.findAssignment(assignmentId);
        return ResponseUtil.success(assignment);
    }

    @GetMapping
    public BasicResponse<AssignmentCountListResponse> findAllAssignment() {
        AssignmentCountListResponse assignments = assignmentService.findAllAssignment();
        return ResponseUtil.success(assignments);
    }

    @PutMapping("/{assignmentId}")
    public BasicResponse<AssignmentResponse> updateAssignment(@PathVariable Long assignmentId, @Valid @RequestBody AssignmentRequest dto) {
        AssignmentResponse assignment = assignmentService.updateAssignment(assignmentId, dto);
        return ResponseUtil.success(assignment);
    }

    @DeleteMapping("/{assignmentId}")
    public BasicResponse<String> deleteAssignment(@PathVariable Long assignmentId) {
        assignmentRegistrationFacade.deleteAssignmentWithSubmission(assignmentId);
        return ResponseUtil.success("과제 삭제에 성공하였습니다.");
    }
}
