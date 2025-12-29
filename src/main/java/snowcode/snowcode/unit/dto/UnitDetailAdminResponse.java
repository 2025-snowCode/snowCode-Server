package snowcode.snowcode.unit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.assignment.dto.AssignmentDetailAdminResponse;

import java.util.List;

public record UnitDetailAdminResponse(
        @Schema(description = "단원 id", example = "1")
        Long id,
        @Schema(description = "단원명", example = "반복문과 조건문")
        String title,
        @Schema(description = "단원 오픈 날짜", example = "2025-11-04")
        String releaseDate,
        @Schema(description = "단원 마감 날짜", example = "2025-11-07")
        String dueDate,
        @Schema(description = "공개 여부", example = "true")
        boolean isOpen,
        @Schema(description = "할당된 과제 수", example = "10")
        int assignmentCount,
        @Schema(description = "할당된 과제 리스트")
        List<AssignmentDetailAdminResponse> assignments) {
}
