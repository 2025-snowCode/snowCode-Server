package snowcode.snowcode.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.auth.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
