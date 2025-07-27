package snowcode.snowcode.unit.dto;

import snowcode.snowcode.unit.domain.Unit;

public record UnitListResponse(Long id, String title, int assignmentCount) {

    public static UnitListResponse from (Unit unit, int assignmentCount) {
        return new UnitListResponse(unit.getId(), unit.getTitle(), assignmentCount);
    }
}
