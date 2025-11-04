package snowcode.snowcode.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.course.domain.Course;

import java.util.List;

public record StudentProgressListResponse(
        @Schema(description = "강의 id", example = "1")
        Long id,
        @Schema(description = "강의명", example = "소프트웨어의이해")
        String title,
        @Schema(description = "분반", example = "001")
        String section,
        @Schema(description = "단원 수", example = "3")
        int unitCount,
        @Schema(description = "강의를 듣는 학생 수", example = "50")
        int studentCount,
        @Schema(description = "학생 리스트 (현 상태 포함)")
        List<StudentProgressResponse> students) {

    public static StudentProgressListResponse of(Course course, int unitCount, List<StudentProgressResponse> students) {
        return new StudentProgressListResponse(course.getId(), course.getTitle(), course.getSection(), unitCount, students.size(), students);
    }
}