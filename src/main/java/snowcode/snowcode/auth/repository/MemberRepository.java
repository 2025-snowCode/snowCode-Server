package snowcode.snowcode.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.auth.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByStudentId(String studentId);

    List<Member> findAllByStudentIdIn(List<String> studentIds);

}
