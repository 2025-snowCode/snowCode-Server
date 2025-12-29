package snowcode.snowcode.auth.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import snowcode.snowcode.auth.domain.Member;

@Builder
public record SignUpResponse(
        @Schema(description = "memberId (integer값으로 구성)", example = "1")
        Long memberId,
        @Schema(description = "회원명, 초기 소셜로그인 진행 시에는 회원 닉네임을 입력받지 않기 때문에 빈 값으로 리턴됩니다.", example = "POZ")
        String name,
        @Schema(description = "jwtToken", example = "ejxfdokwdjzxoas")
        String jwtToken,
        @Schema(description = "refreshToken으로 jwtToken 재발급 가능", example = "jxisdwkd39sdkanxck")
        String refreshToken) {

    public static SignUpResponse of(Member member, String jwtToken, String refreshToken) {
        return new SignUpResponse(member.getId(), member.getName(), jwtToken, refreshToken);
    }
}
