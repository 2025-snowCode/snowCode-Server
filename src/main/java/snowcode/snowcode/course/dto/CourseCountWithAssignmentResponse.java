package snowcode.snowcode.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CourseCountWithAssignmentResponse(
        @Schema(description = "할당된 과제 수", example = "10")
        int count,
        @Schema(description = "할당된 과제 리스트")
        List<CourseWithAssignmentResponse> courses) {
}
