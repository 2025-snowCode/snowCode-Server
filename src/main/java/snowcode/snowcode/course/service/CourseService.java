package snowcode.snowcode.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.common.exception.Validator;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.domain.Semester;
import snowcode.snowcode.course.dto.CourseRequest;
import snowcode.snowcode.course.dto.CourseResponse;
import snowcode.snowcode.course.exception.CourseErrorCode;
import snowcode.snowcode.course.exception.CourseException;
import snowcode.snowcode.course.repository.CourseRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public Course createCourse(CourseRequest dto) {

        int year = Validator.validYear(dto.year());
        Semester semester = Semester.valueOf(dto.semester());

        Course course = Course.createCourse(dto.name(), dto.section(), year, semester, dto.description());
        courseRepository.save(course);
        return course;
    }

    public Course findCourse(Long id) {
        return courseRepository.findById(id).orElseThrow(
                () -> new CourseException(CourseErrorCode.COURSE_NOT_FOUND));
    }

    public CourseResponse updateCourse(Long id, CourseRequest dto) {
        Course course = findCourse(id);

        int year = Validator.validYear(dto.year());
        Semester semester = Semester.valueOf(dto.semester());

        course.updateCourse(dto.name(), dto.section(), year, semester, dto.description());
        return CourseResponse.from(course);
    }

    public void deleteCourse(Long id) {
        Course course = findCourse(id);
        courseRepository.delete(course);
    }
}
