package snowcode.snowcode.refreshToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.refreshToken.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
    Optional<RefreshToken> findByToken(String token);
    void deleteByMemberId(Long memberId);
}
