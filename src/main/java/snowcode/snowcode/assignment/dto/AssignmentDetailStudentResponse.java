package snowcode.snowcode.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AssignmentDetailStudentResponse(
        @Schema(description = "과제 id", example = "1")
        Long id,
        @Schema(description = "과제명", example = "파이썬으로 계산기 구현하기")
        String title,
        @Schema(description = "제출여부", example = "NOT_SUBMITTED")
        String submittedStatus) {
}
