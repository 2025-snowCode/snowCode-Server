package snowcode.snowcode.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.submission.service.SubmissionService;

@Service
@Transactional
@RequiredArgsConstructor
public class AssignmentRegistrationFacade {
    private final AssignmentService assignmentService;
    private final SubmissionService submissionService;

    public void deleteAssignmentWithSubmission(Long assignmentId) {
        submissionService.deleteSubmissionWithAssigmentId(assignmentId);
        assignmentService.deleteAssignment(assignmentId);
    }
}
