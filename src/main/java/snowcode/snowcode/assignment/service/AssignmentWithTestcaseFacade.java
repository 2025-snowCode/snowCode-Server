package snowcode.snowcode.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.AssignmentCreateWithTestcaseRequest;
import snowcode.snowcode.assignment.dto.AssignmentInfoResponse;
import snowcode.snowcode.assignment.dto.AssignmentUpdateWithTestcaseRequest;
import snowcode.snowcode.testcase.dto.TestcaseInfoResponse;
import snowcode.snowcode.testcase.service.TestcaseService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentWithTestcaseFacade {
    private final AssignmentService assignmentService;
    private final TestcaseService testcaseService;

    @Transactional
    public AssignmentInfoResponse createAssignment(Long memberId, AssignmentCreateWithTestcaseRequest dto) {
        Assignment assignment = assignmentService.createAssignment(memberId, dto);
        List<TestcaseInfoResponse> testcases = testcaseService.createTestcases(assignment, dto.testcases());
        return AssignmentInfoResponse.from(assignment, testcases);
    }

    public AssignmentInfoResponse findAssignmentInfo(Long assignmentId) {
        Assignment assignment = assignmentService.findById(assignmentId);
        List<TestcaseInfoResponse> dtoList = testcaseService.findByTestcases(assignmentId);
        return AssignmentInfoResponse.from(assignment, dtoList);
    }

    @Transactional
    public AssignmentInfoResponse updateAssignment(Long id, AssignmentUpdateWithTestcaseRequest dto) {
        Assignment assignment = assignmentService.findById(id);
        assignment.updateAssignment(dto.title(), dto.score(), dto.description());

        testcaseService.deleteTestcaseByAssignmentId(assignment.getId());

        // testcase 다시 추가
        List<TestcaseInfoResponse> testcases = testcaseService.createTestcases(assignment, dto.testcases());
        return AssignmentInfoResponse.from(assignment, testcases);
    }
}
