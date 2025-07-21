package snowcode.snowcode.unit.dto;

import snowcode.snowcode.assignment.dto.AssignmentDetailStudentResponse;

import java.util.List;

public record UnitDetailStudentResponse(Long id, String title, String releaseDate, String dueDate, boolean isOpen, int assignmentCount, List<AssignmentDetailStudentResponse> assignments) {
}
