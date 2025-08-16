package snowcode.snowcode.course.dto;

import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.unit.dto.UnitDetailAdminResponse;

import java.util.List;

public record CourseDetailAdminResponse(Long id, String title, int year, String semester, String section, int studentCount, int unitCount, List<UnitDetailAdminResponse> units) {

    public static CourseDetailAdminResponse of (Course course, int size, List<UnitDetailAdminResponse> units) {
        return new CourseDetailAdminResponse(course.getId(), course.getTitle(), course.getYear(), course.getSemester().toString(), course.getSection(), size, units.size(), units);
    }
}
