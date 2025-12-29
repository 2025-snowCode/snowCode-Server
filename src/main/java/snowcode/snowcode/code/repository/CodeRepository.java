package snowcode.snowcode.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.code.domain.Code;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code, Long> {

    void deleteAllBySubmissionIdIn(List<Long> submissionIdIn);
}
