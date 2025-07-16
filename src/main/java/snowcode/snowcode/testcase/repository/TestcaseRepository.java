package snowcode.snowcode.testcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.testcase.domain.Testcase;

public interface TestcaseRepository extends JpaRepository<Testcase, Long> {
}
