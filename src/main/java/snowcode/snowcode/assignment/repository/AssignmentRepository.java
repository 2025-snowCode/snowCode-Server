package snowcode.snowcode.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snowcode.snowcode.assignment.domain.Assignment;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    //@Query("SELECT u.course.id, COUNT(u) FROM Unit u WHERE u.course.id IN :courseIds GROUP BY u.course.id")
    //    List<Object[]> countUnitsByCourseIds(@Param("courseIds") List<Long> courseIds);


    @Query("SELECT u.course.id, COUNT(a) FROM Assignment a JOIN a.unit u WHERE u.course.id IN :courseIds GROUP BY u.course.id")
    List<Object[]> countAssignmentsByCourseIds(@Param("courseIds") List<Long> courseIds);
}
