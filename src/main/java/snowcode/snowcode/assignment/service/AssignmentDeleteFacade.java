package snowcode.snowcode.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.submission.service.SubmissionWithCodeFacade;
import snowcode.snowcode.testcase.service.TestcaseService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AssignmentDeleteFacade {

    private final AssignmentService assignmentService;
    private final SubmissionWithCodeFacade submissionWithCodeFacade;
    private final TestcaseService testcaseService;
    private final RegistrationService registrationService;

    public void deleteAssignmentWithAll(Long assignmentId) {
        testcaseService.deleteTestcaseByAssignmentId(assignmentId);
        // FIXME - 찾고 삭제하는 로직 중복
        List<AssignmentRegistration> registrations = registrationService.findAllByAssignmentId(assignmentId);
        for (AssignmentRegistration registration : registrations) {
            submissionWithCodeFacade.deleteSubmissionWithRegistrationId(registration.getId());
        }
        registrationService.deleteAllByAssignmentId(assignmentId);
        assignmentService.deleteAssignment(assignmentId);
    }
}
