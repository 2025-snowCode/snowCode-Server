package snowcode.snowcode.unit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.unit.domain.Unit;

public record UnitResponse(
        @Schema(description = "단원 id", example = "1")
        Long id,
        @Schema(description = "단원명", example = "반복문과 조건문")
        String title,
        @Schema(description = "단원 공개일", example = "2025-11-04")
        String releaseDate,
        @Schema(description = "단원 마감일", example = "2025-11-07")
        String dueDate) {

    public static UnitResponse from (Unit unit) {
        return new UnitResponse(unit.getId(), unit.getTitle(), unit.getReleaseDate().toString(), unit.getDueDate().toString());
    }
}
