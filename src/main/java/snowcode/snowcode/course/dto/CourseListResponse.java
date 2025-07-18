package snowcode.snowcode.course.dto;

import snowcode.snowcode.course.domain.Course;

public record CourseListResponse(Long id, int year, String semester, String section, String title, String description, int unitCount, int assignmentCount) {

    public static CourseListResponse create(Course course, int unitCount, int assignmentCount) {
        return new CourseListResponse(
                course.getId(),
                course.getYear(),
                course.getSemester().toString(),
                course.getSection(),
                course.getName(),
                course.getDescription(),
                unitCount,
                assignmentCount
        );
    }
}
