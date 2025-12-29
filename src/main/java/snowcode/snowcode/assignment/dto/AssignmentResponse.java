package snowcode.snowcode.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.assignment.domain.Assignment;

public record AssignmentResponse(
        @Schema(description = "과제 id", example = "1")
        Long id,
        @Schema(description = "과제명", example = "파이썬으로 계산기 만들기")
        String title,
        @Schema(description = "과제 총 점수", example = "100")
        int score,
        @Schema(description = "과제 설명", example = "숫자 두 개를 입력 받으세요.")
        String description) {

    public static AssignmentResponse from (Assignment assignment) {
        return new AssignmentResponse(assignment.getId(), assignment.getTitle(), assignment.getScore(), assignment.getDescription());
    }
}
