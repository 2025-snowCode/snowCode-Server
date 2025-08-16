package snowcode.snowcode.course.dto;

import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.unit.dto.UnitDetailStudentResponse;

import java.util.List;

public record CourseDetailStudentResponse(Long id, String title, int year, String semester, String section, int unitCount, List<UnitDetailStudentResponse> units) {

    public static CourseDetailStudentResponse of (Course course, List<UnitDetailStudentResponse> units) {
        return new CourseDetailStudentResponse(course.getId(), course.getTitle(), course.getYear(), course.getSemester().toString(), course.getSection(), units.size(), units);
    }
}
