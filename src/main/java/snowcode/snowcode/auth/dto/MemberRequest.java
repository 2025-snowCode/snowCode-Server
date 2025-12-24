package snowcode.snowcode.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
        @Schema(description = "회원명", example = "주아정")
        @NotBlank String name,
        @Schema(description = "역할(회원/관리자)", example = "USER")
        @NotBlank String role,
        @Schema(description = "이메일", example = "ajung7038@gmail.com")
        @NotBlank @Email String email) {
}
