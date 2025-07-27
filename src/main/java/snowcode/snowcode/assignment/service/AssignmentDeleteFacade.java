package snowcode.snowcode.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.submission.service.SubmissionWithCodeFacade;
import snowcode.snowcode.testcase.service.TestcaseService;

@Service
@Transactional
@RequiredArgsConstructor
public class AssignmentDeleteFacade {

    private final AssignmentService assignmentService;
    private final SubmissionWithCodeFacade submissionWithCodeFacade;
    private final TestcaseService testcaseService;
    private final RegistrationService registrationService;

    public void deleteAssignmentWithAll(Long assignmentId) {
        testcaseService.deleteTestcaseWithAssignmentId(assignmentId);
        registrationService.deleteAllByAssignmentId(assignmentId);
        submissionWithCodeFacade.deleteSubmissionWithAssigmentId(assignmentId);
        assignmentService.deleteAssignment(assignmentId);
    }
}
