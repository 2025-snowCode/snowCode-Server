package snowcode.snowcode.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import snowcode.snowcode.testcase.dto.TestcaseCreateRequest;

import java.util.List;

public record AssignmentUpdateWithTestcaseRequest(
        @Schema(description = "과제명", example = "파이썬으로 계산기 만들기")
        @NotBlank String title,
        @Schema(description = "과제 총 점수", example = "100")
        @NotNull int score,
        @Schema(description = "과제 설명", example = "두 개의 숫자를 입력 받으세요.")
        String description,
        @Schema(description = "테스트케이스")
        List<TestcaseCreateRequest> testcases) {
}
