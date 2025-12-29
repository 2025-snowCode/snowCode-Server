package snowcode.snowcode.enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snowcode.snowcode.enrollment.domain.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    void deleteByCourseId(Long courseId);
    List<Enrollment> findAllByMemberId(Long memberId);

    List<Enrollment> findAllByCourseId(Long courseId);

    // TODO - STUDY
    @Query("SELECT e.member.id FROM Enrollment e WHERE e.member.id IN :memberIds AND e.course.id = :courseId")
    List<Long> findAlreadyEnrolledMemberIds(@Param("memberIds") List<Long> memberIds, @Param("courseId") Long courseId);

    Optional<Enrollment> findByCourseIdAndMemberId(Long courseId, Long memberId);
}
