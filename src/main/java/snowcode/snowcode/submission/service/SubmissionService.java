package snowcode.snowcode.submission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.submission.domain.Submission;
import snowcode.snowcode.submission.repository.SubmissionRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public Submission createSubmission(Member member, Assignment assignment) {
        Submission submission = Submission.createSubmission(0, member, assignment);
        submissionRepository.save(submission);
        return submission;
    }

    public void deleteSubmissionWithAssigmentId(Long assignmentId) {
        submissionRepository.deleteByAssignmentId(assignmentId);
    }

    public Optional<Submission> isSubmitted(Long memberId, Assignment assignment) {
        return submissionRepository.findByMemberIdAndAssignmentId(memberId, assignment.getId());
    }
}
