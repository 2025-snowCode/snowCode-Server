package snowcode.snowcode.unit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UnitRequest(
        @Schema(description = "단원명", example = "반복문과 조건문")
        @NotBlank String title,
        @Schema(description = "단원 공개일", example = "2025-11-04")
        @NotBlank String releaseDate,
        @Schema(description = "단원 마감일", example = "2025-11-07")
        @NotBlank String dueDate,
        @Schema(description = "할당된 과제 id 리스트")
                          List<Long> assignmentIds) {
}
