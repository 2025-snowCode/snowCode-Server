package snowcode.snowcode.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssignmentRequest(
        @Schema(description = "과제명", example = "파이썬으로 계산기 만들기")
        @NotBlank String title,
        @Schema(description = "과제 총 점수", example = "100")
        @NotNull int score,
        @Schema(description = "과제 설명", example = "숫자 두 개를 입력 받으세요.")
        String description) {
}
