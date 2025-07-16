package snowcode.snowcode.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.AssignmentRequest;
import snowcode.snowcode.assignment.dto.AssignmentResponse;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.submission.service.SubmissionService;

@Service
@Transactional
@RequiredArgsConstructor
public class AssignmentRegistrationFacade {
    private final AssignmentService assignmentService;
    private final SubmissionService submissionService;

    public AssignmentResponse createAssignmentWithSubmission(Member member, AssignmentRequest dto) {
        Assignment assignment = assignmentService.createAssignment(dto);
        submissionService.createSubmission(member, assignment);

        return AssignmentResponse.from(assignment);
    }
}
