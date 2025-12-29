package snowcode.snowcode.assignmentRegistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<AssignmentRegistration, Long> {

    @Query("""
            SELECT u.course.id, COUNT(ar)
            FROM AssignmentRegistration ar
            JOIN ar.unit u
            WHERE u.course.id IN :courseIds
            GROUP BY u.course.id
    """)
    List<Object[]> countAssignmentsByCourseIds(@Param("courseIds") List<Long> courseIds);

    @Query("""
                SELECT a.id AS assignmentId,
                       c.title AS courseTitle,
                       c.section AS courseSection,
                       a.title AS assignmentTitle,
                       u.dueDate AS dueDate
                FROM AssignmentRegistration au
                JOIN au.unit u
                JOIN au.assignment a
                JOIN u.course c
                WHERE u.dueDate BETWEEN :today AND :endDate
                AND NOT EXISTS (
                    SELECT 1
                    FROM Submission s
                    WHERE s.assignmentRegistration = au
                    AND s.member.id = :memberId
                )
            """)
    List<Object[]> findUnsubmittedAssignmentsWithinWeek(@Param("memberId") Long memberId,
                                                        @Param("today") LocalDate today,
                                                        @Param("endDate") LocalDate endDate);

    List<AssignmentRegistration> findAllByUnitId(Long unitId);

    List<AssignmentRegistration> findAllByUnitIdIn(List<Long> unitIds);

    Optional<AssignmentRegistration> findByUnitIdAndAssignmentId(Long unitId, Long assignmentId);

    @Query("""
        SELECT c, a.id, a.title
        FROM AssignmentRegistration ar
        JOIN ar.assignment a
        JOIN ar.unit u
        JOIN u.course c
        WHERE c.id IN :courseIds
    """)
    List<Object[]> findAssignmentsByCourseId(@Param("courseIds") List<Long> courseIds);

    List<AssignmentRegistration> findAllByAssignmentId(Long assignmentId);

    void deleteAllByAssignmentId(Long assignmentId);
    void deleteAllByUnitIdIn(List<Long> unitIds);
    void deleteAllByUnitId(Long unitId);
    void deleteByUnitIdAndAssignmentId(Long unitId, Long assignmentId);


}
