package snowcode.snowcode.unit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.assignment.dto.AssignmentWithScoreResponse;
import snowcode.snowcode.unit.domain.Unit;

import java.time.LocalDate;
import java.util.List;

public record UnitWithAssignmentScoreResponse(
        @Schema(description = "단원 id", example = "2")
        Long id,
        @Schema(description = "단원명", example = "변수와 수식")
        String title,
        @Schema(description = "단원 공개일", example = "2025-11-04")
        String releaseDate,
        @Schema(description = "단원 마감일", example = "2025-11-07")
        String dueDate,
        @Schema(description = "공개 여부", example = "true")
        boolean isOpen,
        @Schema(description = "할당된 과제 수", example = "3")
        int assignmentCount,
        @Schema(description = "할당된 과제 리스트")
        List<AssignmentWithScoreResponse> assignments) {

    public static UnitWithAssignmentScoreResponse of (Unit unit, List<AssignmentWithScoreResponse> assignments) {
        return new UnitWithAssignmentScoreResponse(
                unit.getId(),
                unit.getTitle(),
                unit.getReleaseDate().toString(),
                unit.getDueDate().toString(),
                LocalDate.now().isAfter(unit.getReleaseDate()),
                assignments.size(),
                assignments);
    }
}