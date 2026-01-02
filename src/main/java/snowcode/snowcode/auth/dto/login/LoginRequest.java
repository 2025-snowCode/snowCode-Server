package snowcode.snowcode.auth.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank
                           @Schema(description = "KAKAO", example = "KAKAO")
                           String provider,

                           @Schema(description = "사용자 이름, 없다면 NULL로 넣어주세요!", example = "주아정")
                           String name,

                           @NotBlank
                           @Schema(description = "role, ADMIN or USER", example = "USER")
                           String role,

                           @Schema(description = "email, 소셜로그인에서는 사용 X", example = "test@gmail.com")
                           String email,

                           @Schema(description = "앱에서 소셜 로그인 시 발급 받은 코드", example = "3jdiasjfkdnsizmd")
                           @NotBlank String OAuthToken) {
}