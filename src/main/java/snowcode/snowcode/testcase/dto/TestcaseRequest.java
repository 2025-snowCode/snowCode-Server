package snowcode.snowcode.testcase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TestcaseRequest(
        @Schema(description = "테스트케이스 문제", example = "1+2")
        @NotBlank String testcase,
        @Schema(description = "테스트케이스 정답", example = "3")
        @NotBlank String answer,
        @Schema(description = "테스트케이스/예시 중 하나 택", example = "EXAMPLE")
        @NotBlank String role,
        @Schema(description = "공개 여부 (true이면 공개)", example = "false")
        @NotNull boolean isPublic) {
}
