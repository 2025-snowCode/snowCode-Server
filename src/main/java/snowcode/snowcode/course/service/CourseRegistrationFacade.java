package snowcode.snowcode.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.dto.CourseRequest;
import snowcode.snowcode.course.dto.CourseResponse;
import snowcode.snowcode.enrollment.service.EnrollmentService;
import snowcode.snowcode.unit.service.UnitService;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseRegistrationFacade {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final UnitService unitService;

    public CourseResponse createCourseWithEnroll(Member member, CourseRequest dto) {
        Course course = courseService.createCourse(dto);
        enrollmentService.createEnrollment(member, course);
        return CourseResponse.from(course);
    }

    public void deleteCourseAndEnrollment(Long courseId) {
        enrollmentService.deleteEnrollmentWithCourseId(courseId);
        unitService.deleteUnitWithCourseId(courseId);
        courseService.deleteCourse(courseId);
    }

}
