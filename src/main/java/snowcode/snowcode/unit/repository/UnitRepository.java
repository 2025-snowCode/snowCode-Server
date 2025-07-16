package snowcode.snowcode.unit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.unit.domain.Unit;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    void deleteByCourseId(Long courseId);
}
