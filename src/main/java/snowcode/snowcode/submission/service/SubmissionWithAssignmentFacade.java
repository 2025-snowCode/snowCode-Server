package snowcode.snowcode.submission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.AssignmentDetailAdminResponse;
import snowcode.snowcode.assignment.dto.AssignmentDetailStudentResponse;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.submission.domain.Submission;
import snowcode.snowcode.submission.domain.SubmissionStatus;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionWithAssignmentFacade {

    private final RegistrationService registrationService;
    private final SubmissionPickLastService submissionPickLastService;

    public AssignmentDetailStudentResponse createStudentAssignmentResponse(Long memberId, AssignmentRegistration registration) {
        Assignment assignment = registration.getAssignment();
        String status = getSubmissionStatus(memberId, registration).toString();
        return new AssignmentDetailStudentResponse(assignment.getId(), assignment.getTitle(), status);
    }

    public AssignmentDetailAdminResponse createAdminAssignmentResponse(Long registrationId) {
        AssignmentRegistration registration = registrationService.findById(registrationId);
        Assignment assignment = registration.getAssignment();
        return new AssignmentDetailAdminResponse(assignment.getId(), assignment.getTitle());
    }

    private SubmissionStatus getSubmissionStatus(Long memberId, AssignmentRegistration registration) {
        Optional<Submission> submitted = submissionPickLastService.isSubmitted(memberId, registration);
        return submitted
                .map(s -> s.getScore() == registration.getAssignment().getScore() ? SubmissionStatus.CORRECT : SubmissionStatus.INCORRECT)
                .orElse(SubmissionStatus.NOT_SUBMITTED);
    }
}
