package snowcode.snowcode.unit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.assignment.dto.AssignmentSimpleResponse;
import snowcode.snowcode.unit.domain.Unit;

import java.util.List;

public record UnitWithAssignmentResponse(
        @Schema(description = "단원 id", example = "1")
        Long id,
        @Schema(description = "단원명", example = "반복문과 조건문")
        String title,
        @Schema(description = "단원 공개일", example = "2025-11-04")
        String releaseDate,
        @Schema(description = "단원 마감일", example = "2025-11-07")
        String dueDate,
        @Schema(description = "할당된 과제 수", example = "3")
        int assignmentCount,
        @Schema(description = "할당된 과제")
        List<AssignmentSimpleResponse> assignments) {

    public static UnitWithAssignmentResponse from (Unit unit, List<AssignmentSimpleResponse> assignments) {
        return new UnitWithAssignmentResponse(unit.getId(), unit.getTitle(), unit.getReleaseDate().toString(), unit.getDueDate().toString(), assignments.size(), assignments);
    }
}
