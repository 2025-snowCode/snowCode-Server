package snowcode.snowcode.submission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snowcode.snowcode.submission.domain.Submission;
import snowcode.snowcode.submission.dto.SubmissionScore;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("""
        SELECT s.id
        FROM Submission s
        WHERE s.assignmentRegistration.id = :registrationId""")
    List<Long> findIdsByRegistrationId(@Param("registrationId") Long registrationId);

    List<Submission> findByMemberIdAndAssignmentRegistrationId(Long memberId, Long assignmentRegistrationId);

    @Query("""
    select new snowcode.snowcode.submission.dto.SubmissionScore (
        s.member.id,
        s.assignmentRegistration.id,
        max(s.score)
    )
    from Submission s
    where s.member.id in :memberIds
      and s.assignmentRegistration.id in :registrationIds
    group by s.member.id, s.assignmentRegistration.id
""")
    List<SubmissionScore> findMaxScoresByMemberIdsAndRegistrationIds(
            @Param("memberIds") List<Long> memberIds,
            @Param("registrationIds") List<Long> registrationIds);

}
