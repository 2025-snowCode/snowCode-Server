package snowcode.snowcode.submission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snowcode.snowcode.submission.domain.Submission;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("""
        SELECT s.id
        FROM Submission s
        WHERE s.assignment.id = :assignmentId""")
    List<Long> findIdsByAssignmentId(@Param("assignmentId") Long assignmentId);

    Optional<Submission> findByMemberIdAndAssignmentId(Long memberId, Long assignmentId);
}
