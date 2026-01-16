package snowcode.snowcode.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CodeRequestSocket(
        @Schema(description = "사용자가 입력한 input, 없을 경우 \"\"로 표시", example = "\"\"") String input,
        @NotBlank @Schema(description = "코드", example = "print(\"Hello, World!\")") String code,
        @Schema(description = "언어", example = "PYTHON") @NotBlank String language

) {
}