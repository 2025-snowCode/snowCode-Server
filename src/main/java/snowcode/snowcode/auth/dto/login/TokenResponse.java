package snowcode.snowcode.auth.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(
        @Schema(description = "newAccessToken, 기존 refreshToken을 갖고 재발급한 새 토큰", example = "ejxfdokwdjzxoas")
        String newAccessToken,
        @Schema(description = "refreshToken, 기존 사용 값으로 다시 들어감 (같은 값)", example = "ejxfdokwdjzxoas")
        String refreshToken) {
}
