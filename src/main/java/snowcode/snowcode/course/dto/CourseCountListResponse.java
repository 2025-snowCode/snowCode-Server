package snowcode.snowcode.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CourseCountListResponse(
        @Schema(description = "강의 개수", example = "3")
        int count,
        @Schema(description = "강의 리스트")
        List<CourseListResponse> courses) {
}
