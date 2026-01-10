package snowcode.snowcode.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CodeRequest(
        @Schema(description = "코드", example = "a, b = map(int, input().split()) \nprint(a + b)")
        @NotBlank String code,
        @Schema(description = "언어", example = "PYTHON")
        @NotBlank String language) {
}
