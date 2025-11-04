package snowcode.snowcode.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.unit.dto.UnitDetailStudentResponse;

import java.util.List;

public record CourseDetailStudentResponse(
        @Schema(description = "강의 id", example = "1")
        Long id,
        @Schema(description = "강의명", example = "소프트웨어의이해")
        String title,
        @Schema(description = "개설 연도", example = "2025")
        int year,
        @Schema(description = "개설 학기", example = "SUMMER")
        String semester,
        @Schema(description = "분반", example = "001")
        String section,
        @Schema(description = "단원 수", example = "3")
        int unitCount,
        @Schema(description = "단원 리스트")
        List<UnitDetailStudentResponse> units) {

    public static CourseDetailStudentResponse of (Course course, List<UnitDetailStudentResponse> units) {
        return new CourseDetailStudentResponse(course.getId(), course.getTitle(), course.getYear(), course.getSemester().toString(), course.getSection(), units.size(), units);
    }
}
