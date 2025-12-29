package snowcode.snowcode.auth.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;
import snowcode.snowcode.auth.dto.login.*;
import snowcode.snowcode.auth.dto.login.kakao.KakaoUserResponse;
import snowcode.snowcode.auth.exception.TokenErrorCode;
import snowcode.snowcode.auth.exception.TokenException;
import snowcode.snowcode.auth.repository.MemberRepository;
import snowcode.snowcode.refreshToken.service.RefreshTokenService;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.security.core.userdetails.UserDetails;


import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final WebClient webClient;
    private final RefreshTokenService refreshTokenService;


    @Transactional
    public SignUpResponse login(LoginRequest dto) throws GeneralSecurityException {


        // 1. providerId 획득
        UserResponse userResponse = switch (dto.provider()) {
            case "KAKAO" -> kakaoLogin(dto);
            default -> throw new TokenException(TokenErrorCode.INVALID_PROVIDER);
        };

        String providerId = userResponse.providerId();
        if (providerId.isBlank()) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN);
        }

        // 2. DB 조회
        Member member = memberRepository.findByProviderAndProviderId(dto.provider(), providerId)
                .orElseGet(() -> signUp(dto, userResponse));

        CustomUserInfoDto info = CustomUserInfoDto.of(member);

        // 3. accessToken, refreshToken 발급
        String accessToken = jwtUtil.createAccessToken(info); // access token
        String refreshToken = jwtUtil.createRefreshToken(info); // refresh token

        // 4. RefreshToken 저장/업데이트
        LocalDateTime expiryAt = LocalDateTime.now().plusDays(14);

        refreshTokenService.saveOrUpdate(member, refreshToken, expiryAt);

        return SignUpResponse.of(member, accessToken, refreshToken);
    }

    @Transactional
    public Member signUp(LoginRequest dto, UserResponse userResponse) {
        Member member = Member.createMember(dto, userResponse);
        memberRepository.save(member);
        return member;
    }

    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByUsername(UUID.fromString(username))
                .orElseThrow(() -> new TokenException(TokenErrorCode.UNAUTHORIZED));

        return new CustomUserDetails(member);
    }

    public Member loadMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            throw new TokenException(TokenErrorCode.UNAUTHORIZED);
        }

        return memberRepository.findById(user.getMemberId())
                .orElseThrow(() -> new TokenException(TokenErrorCode.UNAUTHORIZED));
    }

    public Role getRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            throw new TokenException(TokenErrorCode.UNAUTHORIZED);
        }
        return user.getRole();
    }


    /**
     카카오 로그인
     **/
    public UserResponse kakaoLogin(LoginRequest dto) {
        KakaoUserResponse response = getKakaoUserInfo(dto.OAuthToken());
        return UserResponse.of(response);
    }


    private KakaoUserResponse getKakaoUserInfo(String accessToken) {
        return webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserResponse.class) // JSON → DTO 변환
                .block(); // 동기 방식으로 결과 받기
    }

}
