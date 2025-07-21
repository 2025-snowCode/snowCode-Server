package snowcode.snowcode.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.dto.CourseResponse;
import snowcode.snowcode.student.service.StudentService;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseWithStudentFacade {
    private final StudentService studentService;

}
