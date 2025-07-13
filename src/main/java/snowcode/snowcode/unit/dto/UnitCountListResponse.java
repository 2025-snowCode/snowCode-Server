package snowcode.snowcode.unit.dto;

import java.util.List;

public record UnitCountListResponse (int count, List<UnitListResponse> units) {
}
