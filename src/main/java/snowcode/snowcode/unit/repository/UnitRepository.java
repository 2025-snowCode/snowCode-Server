package snowcode.snowcode.unit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snowcode.snowcode.unit.domain.Unit;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    @Query("SELECT u.course.id, COUNT(u) FROM Unit u WHERE u.course.id IN :courseIds GROUP BY u.course.id")
    List<Object[]> countUnitsByCourseIds(@Param("courseIds") List<Long> courseIds);

    List<Unit> findAllByCourseId(Long courseId);

    @Query("SELECT u.id FROM Unit u WHERE u.course.id = :courseId")
    List<Long> findIdByCourseId(@Param("courseId") Long courseId);

    @Query("""
        SELECT u.id, u.title, COUNT(ar)
        FROM Unit u
        LEFT JOIN AssignmentRegistration ar ON ar.unit = u
        WHERE u.course.id = :courseId
        GROUP BY u.id, u.title
    """)
    List<Object[]> findUnitIdTitleAndAssignmentCount(@Param("courseId") Long courseId);
}
