package snowcode.snowcode.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.assignment.domain.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

}
