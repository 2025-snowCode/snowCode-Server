package snowcode.snowcode.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.assignment.domain.Assignment;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findAllByCreatedBy(Long createdBy);
}
