package snowcode.snowcode.unit.dto;

import snowcode.snowcode.unit.domain.Unit;

public record UnitResponse(Long id, String title, String releaseDate, String dueDate) {

    public static UnitResponse from (Unit unit) {
        return new UnitResponse(unit.getId(), unit.getTitle(), unit.getReleaseDate().toString(), unit.getDueDate().toString());
    }
}
