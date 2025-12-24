package snowcode.snowcode.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.assignment.dto.AssignmentSimpleResponse;
import snowcode.snowcode.course.domain.Course;

import java.util.List;

public record CourseWithAssignmentResponse(
        @Schema(description = "강의 id", example = "1")
        Long id,
        @Schema(description = "강의명", example = "소프트웨어의이해")
        String title,
        @Schema(description = "개설 연도", example = "2025")
        int year,
        @Schema(description = "학기", example = "SUMMER")
        String semester,
        @Schema(description = "분반", example = "001")
        String section,
        @Schema(description = "할당된 과제 수", example = "10")
        int count,
        @Schema(description = "할당된 과제 리스트")
        List<AssignmentSimpleResponse> assignments) {

    public static CourseWithAssignmentResponse create(Course course, List<AssignmentSimpleResponse> assignments) {
        return new CourseWithAssignmentResponse(course.getId(), course.getTitle(), course.getYear(), course.getSemester().toString(), course.getSection(), assignments.size(), assignments);
    }
}
