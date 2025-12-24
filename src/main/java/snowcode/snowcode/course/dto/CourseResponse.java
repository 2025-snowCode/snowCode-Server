package snowcode.snowcode.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.course.domain.Course;

public record CourseResponse(
        @Schema(description = "강의 id", example = "1")
        Long id,
        @Schema(description = "강의명", example = "소프트웨어의이해")
        String title,
        @Schema(description = "분반", example = "001")
        String section,
        @Schema(description = "강의 개설 연도", example = "2025")
        int year,
        @Schema(description = "개설 학기", example = "SUMMER")
        String semester,
        @Schema(description = "강의 소개", example = "소프트웨어의이해는 파이썬을 배웁니다.")
        String description) {

    public static CourseResponse from(Course course) {
        return new CourseResponse(course.getId(), course.getTitle(), course.getSection(), course.getYear(), course.getSemester().toString(), course.getDescription());
    }
}
