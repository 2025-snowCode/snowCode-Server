package snowcode.snowcode.submission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.submission.domain.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    void deleteByAssignmentId(Long assignmentId);
}
