package snowcode.snowcode.course.dto;

import snowcode.snowcode.unit.dto.UnitDetailStudentResponse;

import java.util.List;

public record CourseDetailStudentResponse(Long id, String title, int year, String semester, String section, int unitCount, List<UnitDetailStudentResponse> units) {
}
