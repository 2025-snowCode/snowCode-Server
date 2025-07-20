package snowcode.snowcode.testcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.testcase.domain.Testcase;

import java.util.List;

public interface TestcaseRepository extends JpaRepository<Testcase, Long> {
    List<Testcase> findByAssignmentId(Long assignmentId);
}
