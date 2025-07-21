package snowcode.snowcode.submission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.AssignmentDetailAdminResponse;
import snowcode.snowcode.assignment.dto.AssignmentDetailStudentResponse;
import snowcode.snowcode.assignment.service.AssignmentService;
import snowcode.snowcode.submission.domain.Submission;
import snowcode.snowcode.submission.domain.SubmissionStatus;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionWithAssignmentFacade {

    private final SubmissionService submissionService;
    private final AssignmentService assignmentService;

    public AssignmentDetailStudentResponse createStudentAssignmentResponse(Long memberId, Long assignmentId) {
        Assignment assignment = assignmentService.findById(assignmentId);
        String status = getSubmissionStatus(memberId, assignment).toString();
        return new AssignmentDetailStudentResponse(assignmentId, assignment.getTitle(), status);
    }

    public AssignmentDetailAdminResponse createAdminAssignmentResponse(Long assignmentId) {
        Assignment assignment = assignmentService.findById(assignmentId);
        return new AssignmentDetailAdminResponse(assignmentId, assignment.getTitle());
    }

    private SubmissionStatus getSubmissionStatus(Long memberId, Assignment assignment) {
        Optional<Submission> submitted = submissionService.isSubmitted(memberId, assignment);
        return submitted
                .map(s -> s.getScore() == assignment.getScore() ? SubmissionStatus.CORRECT : SubmissionStatus.INCORRECT)
                .orElse(SubmissionStatus.NOT_SUBMITTED);
    }
}
