package snowcode.snowcode.unit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snowcode.snowcode.unit.domain.Unit;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    void deleteByCourseId(Long courseId);

    @Query("SELECT u.course.id, COUNT(u) FROM Unit u WHERE u.course.id IN :courseIds GROUP BY u.course.id")
    List<Object[]> countUnitsByCourseIds(@Param("courseIds") List<Long> courseIds);
}
