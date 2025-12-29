package snowcode.snowcode.auth.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ReissueRequest(@NotBlank
                             @Schema(description = "만료되지 않은 refreshToken, 만약 refreshToken이 만료되었다면 Exception, 이는 로그인을 다시 해야 합니다.", example = "dsnciw3j9dsckja")
                             String refreshToken) {
}