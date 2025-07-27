package snowcode.snowcode.submission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.submission.domain.Submission;
import snowcode.snowcode.submission.repository.SubmissionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public Submission createSubmission(Member member, Assignment assignment) {
        Submission submission = Submission.createSubmission(0, member, assignment);
        submissionRepository.save(submission);
        return submission;
    }

    @Transactional(readOnly = true)
    public Optional<Submission> isSubmitted(Long memberId, Assignment assignment) {
        return submissionRepository.findByMemberIdAndAssignmentId(memberId, assignment.getId());
    }

    @Transactional(readOnly = true)
    public List<Long> findAllByAssignmentId(Long assignmentId) {
        return submissionRepository.findIdsByAssignmentId(assignmentId);
    }

    public void deleteAllById(List<Long> submissionIds) {
        submissionRepository.deleteAllById(submissionIds);
    }
}
