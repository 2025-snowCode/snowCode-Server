package snowcode.snowcode.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.assignment.domain.Assignment;

public record AssignmentSimpleResponse(
        @Schema(description = "과제 id", example = "1")
        Long id,
        @Schema(description = "과제명", example = "파이썬으로 계산기 만들기")
        String title) {

    public static AssignmentSimpleResponse from(Assignment assignment) {
        return new AssignmentSimpleResponse(assignment.getId(), assignment.getTitle());
    }
}
