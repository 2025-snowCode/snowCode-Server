package snowcode.snowcode.unit.dto;

import snowcode.snowcode.assignment.dto.AssignmentWithScoreResponse;
import snowcode.snowcode.unit.domain.Unit;

import java.time.LocalDate;
import java.util.List;

public record UnitWithAssignmentScoreResponse(Long id, String title, String releaseDate, String dueDate, boolean isOpen, int assignmentCount, List<AssignmentWithScoreResponse> assignments) {

    public static UnitWithAssignmentScoreResponse of (Unit unit, List<AssignmentWithScoreResponse> assignments) {
        return new UnitWithAssignmentScoreResponse(
                unit.getId(),
                unit.getTitle(),
                unit.getReleaseDate().toString(),
                unit.getDueDate().toString(),
                LocalDate.now().isAfter(unit.getReleaseDate()),
                assignments.size(),
                assignments);
    }
}