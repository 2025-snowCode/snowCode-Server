package snowcode.snowcode.refreshToken.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.exception.TokenErrorCode;
import snowcode.snowcode.auth.exception.TokenException;
import snowcode.snowcode.refreshToken.domain.RefreshToken;
import snowcode.snowcode.refreshToken.repository.RefreshTokenRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // 로그인 시 Refresh Token 저장 (있으면 업데이트, 없으면 생성)
    @Transactional
    public void saveOrUpdate(Member member, String token, LocalDateTime expiryAt) {
        refreshTokenRepository.findByMember(member)
                .ifPresentOrElse(
                        rt -> rt.updateToken(token, expiryAt),
                        () -> refreshTokenRepository.save(RefreshToken.create(member, token, expiryAt))
                );
    }

    // Refresh Token 검증
    public Member validateAndGetMember(String token) {
        RefreshToken rt = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenException(TokenErrorCode.INVALID_TOKEN));

        if (rt.getExpiryAt().isBefore(LocalDateTime.now())) {
            throw new TokenException(TokenErrorCode.TOKEN_HAS_EXPIRED);
        }

        return rt.getMember();
    }

    // 로그아웃 (Refresh Token 삭제)
    @Transactional
    public void deleteByMemberId(Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }
}
