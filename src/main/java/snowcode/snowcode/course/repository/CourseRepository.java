package snowcode.snowcode.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.course.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
