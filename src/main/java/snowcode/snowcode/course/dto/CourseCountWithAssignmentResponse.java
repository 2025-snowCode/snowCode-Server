package snowcode.snowcode.course.dto;

import java.util.List;

public record CourseCountWithAssignmentResponse(int count, List<CourseWithAssignmentResponse> courses) {
}
