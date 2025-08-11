package snowcode.snowcode.code.dto;

import jakarta.validation.constraints.NotBlank;

public record CodeRequest(@NotBlank String code, @NotBlank String language) {
}
