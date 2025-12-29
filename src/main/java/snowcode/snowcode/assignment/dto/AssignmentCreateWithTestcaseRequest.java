package snowcode.snowcode.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import snowcode.snowcode.testcase.dto.TestcaseCreateRequest;

import java.util.List;

public record AssignmentCreateWithTestcaseRequest(
        @Schema(description = "과제 제목", example = "소프트웨어의 이해")
        @NotBlank String title,
        @Schema(description = "과제 총 점수", example = "100")
        @NotNull int score,
        @Schema(description = "과제 설명", example = "파이썬으로 계산기 구현하기")
        String description,
        @Schema(description = "테스트케이스")
        List<TestcaseCreateRequest> testcases) {
}
