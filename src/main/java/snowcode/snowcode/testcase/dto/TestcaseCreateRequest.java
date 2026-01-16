package snowcode.snowcode.testcase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TestcaseCreateRequest(
        @Schema(description = "테스트케이스 문제", example = "1 2")
        @NotBlank String testcase,
        @Schema(description = "테스트케이스 정답", example = "3")
        @NotBlank String answer) {
}
