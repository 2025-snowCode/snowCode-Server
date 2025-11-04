package snowcode.snowcode.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AssignmentDetailAdminResponse(
        @Schema(description = "과제 id", example = "1")
        Long id,
        @Schema(description = "과제명", example = "파이썬으로 날씨 알아보기")
        String title) {
}
