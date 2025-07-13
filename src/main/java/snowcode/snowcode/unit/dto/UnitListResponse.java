package snowcode.snowcode.unit.dto;

import snowcode.snowcode.unit.domain.Unit;

public record UnitListResponse(String title) {

    public static UnitListResponse from (Unit unit) {
        return new UnitListResponse(unit.getTitle());
    }
}
