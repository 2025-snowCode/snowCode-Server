package snowcode.snowcode.student.dto;

import snowcode.snowcode.course.domain.Course;

import java.util.List;

public record StudentProgressListResponse(Long id, String title, String section, int unitCount, int studentCount, List<StudentProgressResponse> students) {

    public static StudentProgressListResponse of(Course course, int unitCount, List<StudentProgressResponse> students) {
        return new StudentProgressListResponse(course.getId(), course.getTitle(), course.getSection(), unitCount, students.size(), students);
    }
}