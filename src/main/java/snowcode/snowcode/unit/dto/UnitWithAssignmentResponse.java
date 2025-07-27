package snowcode.snowcode.unit.dto;

import snowcode.snowcode.assignment.dto.AssignmentSimpleResponse;
import snowcode.snowcode.unit.domain.Unit;

import java.util.List;

public record UnitWithAssignmentResponse(Long id, String title, String releaseDate, String dueDate, int assignmentCount, List<AssignmentSimpleResponse> assignments) {

    public static UnitWithAssignmentResponse from (Unit unit, List<AssignmentSimpleResponse> assignments) {
        return new UnitWithAssignmentResponse(unit.getId(), unit.getTitle(), unit.getReleaseDate().toString(), unit.getDueDate().toString(), assignments.size(), assignments);
    }
}
