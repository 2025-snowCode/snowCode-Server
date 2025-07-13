package snowcode.snowcode.course.dto;

import snowcode.snowcode.course.domain.Course;

public record CourseResponse(Long id, String name, String section, int year, String semester, String description) {

    public static CourseResponse from(Course course) {
        return new CourseResponse(course.getId(), course.getName(), course.getSection(), course.getYear(), course.getSemester().toString(), course.getDescription());
    }
}
