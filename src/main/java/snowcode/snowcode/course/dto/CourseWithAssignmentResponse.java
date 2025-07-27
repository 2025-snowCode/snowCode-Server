package snowcode.snowcode.course.dto;

import snowcode.snowcode.assignment.dto.AssignmentSimpleResponse;
import snowcode.snowcode.course.domain.Course;

import java.util.List;

public record CourseWithAssignmentResponse(Long id, String title, int year, String semester, String section, int count, List<AssignmentSimpleResponse> assignments) {

    public static CourseWithAssignmentResponse create(Course course, List<AssignmentSimpleResponse> assignments) {
        return new CourseWithAssignmentResponse(course.getId(), course.getTitle(), course.getYear(), course.getSemester().toString(), course.getSection(), assignments.size(), assignments);
    }
}
