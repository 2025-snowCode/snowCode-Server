package snowcode.snowcode.enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.enrollment.domain.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    void deleteByCourseId(Long courseId);
}
