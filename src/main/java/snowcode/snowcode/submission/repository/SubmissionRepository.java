package snowcode.snowcode.submission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.submission.domain.Submission;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    void deleteByAssignmentId(Long assignmentId);

    Optional<Submission> findByMemberIdAndAssignmentId(Long memberId, Long assignmentId);
}
