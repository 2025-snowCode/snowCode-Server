package snowcode.snowcode.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snowcode.snowcode.assignment.domain.Assignment;

import java.time.LocalDate;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query("SELECT u.course.id, COUNT(a) FROM Assignment a JOIN a.unit u WHERE u.course.id IN :courseIds GROUP BY u.course.id")
    List<Object[]> countAssignmentsByCourseIds(@Param("courseIds") List<Long> courseIds);

    @Query("""
            SELECT c.title AS courseTitle,
                c.section AS courseSection,
                a.title AS assignmentName,
                u.dueDate AS dueDate
            FROM Assignment a
            JOIN a.unit u
            JOIN u.course c
            WHERE u.dueDate BETWEEN :today AND :endDate
            AND NOT EXISTS (
                SELECT 1
                FROM Submission s
                WHERE s.assignment = a
                AND s.member.id =:memberId
            )
            """)
    List<Object[]> findUnsubmittedAssignmentsWithinWeek(@Param("memberId") Long memberId, @Param("today") LocalDate today, @Param("endDate") LocalDate endDate);

    List<Assignment> findAllByUnitId(@Param("unitId") Long unitId);
}
