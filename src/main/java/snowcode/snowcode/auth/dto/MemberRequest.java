package snowcode.snowcode.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest(@NotBlank String name,
                            @NotBlank String role,
                            @Email String email) {
}
