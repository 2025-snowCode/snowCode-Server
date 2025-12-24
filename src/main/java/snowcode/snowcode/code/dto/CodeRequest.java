package snowcode.snowcode.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CodeRequest(
        @Schema(description = "코드", example = "#include <stdio.h> /n int main() { return 1+2; }")
        @NotBlank String code,
        @Schema(description = "언어", example = "C")
        @NotBlank String language) {
}
