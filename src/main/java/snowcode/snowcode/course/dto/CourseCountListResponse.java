package snowcode.snowcode.course.dto;

import java.util.List;

public record CourseCountListResponse(int count, List<CourseListResponse> courses) {
}
