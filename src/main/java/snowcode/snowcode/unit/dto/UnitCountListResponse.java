package snowcode.snowcode.unit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UnitCountListResponse (
        @Schema(description = "단원 수", example = "3")
        int count,
        @Schema(description = "단원 리스트")
        List<UnitListResponse> units) {
}
