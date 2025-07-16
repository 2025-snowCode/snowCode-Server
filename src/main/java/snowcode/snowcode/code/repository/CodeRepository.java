package snowcode.snowcode.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.code.domain.Code;

public interface CodeRepository extends JpaRepository<Code, Long> {
}
