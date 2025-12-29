package snowcode.snowcode.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snowcode.snowcode.course.domain.Course;

import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
        SELECT c.id
        FROM Course c
        JOIN Enrollment e ON e.course.id = c.id
        WHERE e.member.id = :memberId
            AND c.title = :title
    """)
    List<Long> findIdsByTitle(@Param("memberId") Long memberId, @Param("title") String title);
}
