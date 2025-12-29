package snowcode.snowcode.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.assignment.domain.Assignment;

public record AssignmentListResponse(
        @Schema(description = "과제 id", example = "1")
        Long id,
        @Schema(description = "과제 제목", example = "소프트웨어의 이해")
        String title,
        @Schema(description = "과제 점수", example = "30")
        int score) {

    public static AssignmentListResponse from (Assignment assignment) {
        return new AssignmentListResponse(assignment.getId(), assignment.getTitle(), assignment.getScore());
    }
}
