package snowcode.snowcode.unit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.unit.domain.Unit;

public record UnitListResponse(
        @Schema(description = "단원 id", example = "1")
        Long id,
        @Schema(description = "단원명", example = "반복문과 변수")
        String title,
        @Schema(description = "할당된 과제 수", example = "3")
        int assignmentCount) {

    public static UnitListResponse from (Unit unit, int assignmentCount) {
        return new UnitListResponse(unit.getId(), unit.getTitle(), assignmentCount);
    }
}
