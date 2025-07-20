package snowcode.snowcode.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.AssignmentInfoResponse;
import snowcode.snowcode.testcase.dto.TestcaseInfoResponse;
import snowcode.snowcode.testcase.service.TestcaseService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentWithTestcaseFacade {
    private final AssignmentService assignmentService;
    private final TestcaseService testcaseService;

    public AssignmentInfoResponse findAssignmentInfo(Long assignmentId) {
        Assignment assignment = assignmentService.findById(assignmentId);
        List<TestcaseInfoResponse> dtoList = testcaseService.findByTestcases(assignmentId);
        return AssignmentInfoResponse.of(assignment, dtoList);
    }
}
