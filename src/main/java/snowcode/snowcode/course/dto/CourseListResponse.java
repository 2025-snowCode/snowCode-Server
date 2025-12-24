package snowcode.snowcode.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.course.domain.Course;

public record CourseListResponse(
        @Schema(description = "강의 id", example = "1")
        Long id,
        @Schema(description = "강의 연도", example = "2025")
        int year,
        @Schema(description = "학기", example = "SUMMER")
        String semester,
        @Schema(description = "분반", example = "001")
        String section,
        @Schema(description = "강의명", example = "소프트웨어의이해")
        String title,
        @Schema(description = "강의 소개", example = "소프트웨어의이해 강의는 파이썬을 배웁니다.")
        String description,
        @Schema(description = "단원 수", example = "3")
        int unitCount,
        @Schema(description = "할당된 과제 수", example = "10")
        int assignmentCount) {

    public static CourseListResponse create(Course course, int unitCount, int assignmentCount) {
        return new CourseListResponse(
                course.getId(),
                course.getYear(),
                course.getSemester().toString(),
                course.getSection(),
                course.getTitle(),
                course.getDescription(),
                unitCount,
                assignmentCount
        );
    }
}
