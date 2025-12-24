package snowcode.snowcode.unit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UnitProgressResponse(
        @Schema(description = "단원 상태 (전부 맞았을 시에만 CORRECT)", example = "CORRECT")
        String status) {
}
