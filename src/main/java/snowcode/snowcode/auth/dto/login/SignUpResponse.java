package snowcode.snowcode.auth.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import snowcode.snowcode.auth.domain.Member;

@Builder
public record SignUpResponse(
        @Schema(description = "memberId (integer값으로 구성)", example = "1")
        Long memberId,
        @Schema(description = "회원명, 초기 소셜로그인 진행 시에는 회원 닉네임을 입력받지 않기 때문에 UUID 값으로 리턴됩니다.", example = "SnowCode")
        String name,
        @Schema(description = "역할, USER or ADMIN", example = "ADMIN")
        String role,
        @Schema(description = "학번", example = "2313398")
        String studentId,
        @Schema(description = "이메일", example = "snowcode@gmail.com")
        String email,
        @Schema(description = "provider (KAKAO 고정)", example = "KAKAO")
        String provider,
        @Schema(description = "accessToken, Swagger 확인용, 유효기간: 1일", example = "sM0yOK1FPuGJaq8x/U76gkKNfT64GQKsityED54zG9M=")
        String accessToken
) {

    public static SignUpResponse of(Member member, String jwt) {
        return new SignUpResponse(member.getId(), member.getName(), member.getRole().toString(), member.getStudentId(), member.getEmail(), member.getProvider(), jwt);
    }
}
