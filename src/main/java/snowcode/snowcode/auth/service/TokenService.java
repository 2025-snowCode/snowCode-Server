package snowcode.snowcode.auth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.dto.login.CustomUserInfoDto;
import snowcode.snowcode.auth.dto.login.TokenResponse;
import snowcode.snowcode.auth.exception.TokenErrorCode;
import snowcode.snowcode.auth.exception.TokenException;
import snowcode.snowcode.refreshToken.domain.RefreshToken;
import snowcode.snowcode.refreshToken.repository.RefreshTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;

    @Transactional
    public TokenResponse refresh(String refreshToken) {
        // 1. JWT 서명 검증 & 만료 확인
        Claims claims = jwtUtil.parseClaims(refreshToken);
        UUID username = UUID.fromString(claims.getSubject());
        Member member = memberService.findMemberByUsername(username);

        // 2. DB에 저장된 RefreshToken 확인
        RefreshToken saved = refreshTokenRepository.findByMember(member)
                .orElseThrow(() -> new TokenException(TokenErrorCode.NOT_FOUND_TOKEN));

        if (!saved.getToken().equals(refreshToken)) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN);
        }
        if (saved.getExpiryAt().isBefore(LocalDateTime.now())) {
            throw new TokenException(TokenErrorCode.TOKEN_HAS_EXPIRED);
        }

        // 3. 새 AccessToken 발급
        String newAccessToken = jwtUtil.createAccessToken(CustomUserInfoDto.of(member));

        return new TokenResponse(newAccessToken, refreshToken); // Refresh는 그대로 재사용
    }

}
