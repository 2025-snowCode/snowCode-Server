package snowcode.snowcode.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.domain.Semester;
import snowcode.snowcode.course.dto.CourseCreateRequest;
import snowcode.snowcode.course.dto.CourseResponse;
import snowcode.snowcode.course.repository.CourseRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    @Transactional
    public CourseResponse createCourse(CourseCreateRequest dto) {
        Course course = Course.createCourse(dto.name(), dto.section(), dto.year(), Semester.of(dto.semester()), dto.description());
        courseRepository.save(course);
        return CourseResponse.from(course);
    }
}
