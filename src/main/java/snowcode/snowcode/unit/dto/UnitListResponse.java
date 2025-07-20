package snowcode.snowcode.unit.dto;

import snowcode.snowcode.unit.domain.Unit;

public record UnitListResponse(Long id, String title, String releaseDate, String dueDate) {

    public static UnitListResponse from (Unit unit) {
        return new UnitListResponse(unit.getId(), unit.getTitle(), unit.getReleaseDate().toString(), unit.getDueDate().toString());
    }
}
