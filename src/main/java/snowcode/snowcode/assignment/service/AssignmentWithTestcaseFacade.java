package snowcode.snowcode.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.AssignmentCreateWithTestcaseRequest;
import snowcode.snowcode.assignment.dto.AssignmentInfoResponse;
import snowcode.snowcode.assignment.dto.AssignmentRequest;
import snowcode.snowcode.assignment.dto.AssignmentResponse;
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
    public AssignmentInfoResponse createAssignment(AssignmentCreateWithTestcaseRequest dto) {

        Assignment assignment = assignmentService.createAssignment(dto.title(), dto.score(), dto.description());
        List<TestcaseInfoResponse> testcases = testcaseService.createTestcases(assignment, dto.testcases());

        return AssignmentInfoResponse.of(assignment, testcases);
    }

    public AssignmentInfoResponse findAssignmentInfo(Long assignmentId) {
        Assignment assignment = assignmentService.findById(assignmentId);
        List<TestcaseInfoResponse> dtoList = testcaseService.findByTestcases(assignmentId);
        return AssignmentInfoResponse.of(assignment, dtoList);
    }
}
