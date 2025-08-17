package snowcode.snowcode.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;
import snowcode.snowcode.auth.exception.AuthErrorCode;
import snowcode.snowcode.auth.exception.AuthException;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.dto.CourseCountListResponse;
import snowcode.snowcode.course.dto.CourseListResponse;
import snowcode.snowcode.course.dto.CourseRequest;
import snowcode.snowcode.course.dto.CourseResponse;
import snowcode.snowcode.enrollment.domain.Enrollment;
import snowcode.snowcode.enrollment.service.EnrollmentService;
import snowcode.snowcode.student.dto.StudentRequest;
import snowcode.snowcode.student.service.StudentService;
import snowcode.snowcode.unit.service.UnitService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseWithEnrollmentFacade {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final UnitService unitService;
    private final StudentService studentService;
    private final RegistrationService registrationService;

    public CourseResponse createCourseWithEnroll(Member member, CourseRequest dto) {
        Course course = courseService.createCourse(dto);
        List<Member> members = studentService.findStudents(dto.students());
        studentService.addAdminInMembers(member, members);
        enrollmentService.createEnrollment(members, course);
        return CourseResponse.from(course);
    }

    public void addStudentWithEnroll(Long courseId, StudentRequest dto) {
        Member student = studentService.findByStudentId(dto.studentId());
        Course course = courseService.findCourse(courseId);
        boolean isAlreadyEnrolled = enrollmentService.isAlreadyEnrolled(courseId, student.getId());

        if (isAlreadyEnrolled) throw new AuthException(AuthErrorCode.IS_ALREADY_ENROLLED_STUDENT);
        enrollmentService.createEnrollment(student, course);
    }

    public void deleteCourseAndEnrollment(Long courseId) {
        List<Long> unitIds = unitService.findIdsByCourseId(courseId);
        registrationService.deleteAllByUnitIdIn(unitIds);
        unitService.deleteAllById(unitIds);
        enrollmentService.deleteEnrollmentWithCourseId(courseId);
        courseService.deleteCourse(courseId);
    }

    public void deleteStudentWithEnrollment(Long courseId, Long memberId) {
        Enrollment enrollment = enrollmentService.findByMemberIdAndCourseId(courseId, memberId);
        enrollmentService.deleteEnrollment(enrollment);
    }


    @Transactional(readOnly = true)
    public CourseCountListResponse findMyCourses(Long memberId) {
        List<Enrollment> enrollmentList = enrollmentService.findByMemberId(memberId);
        List<Course> courses = enrollmentService.findCoursesByEnrollment(enrollmentList);
        List<Long> courseIds = courseService.extractCourseIds(courses);

        Map<Long, Integer> unitMap = unitService.countUnitsByCourseId(courseIds);
        Map<Long, Integer> assignmentMap = registrationService.countAssignmentsByCourseId(courseIds);

        List<CourseListResponse> dtoList = new ArrayList<>();
        for (Course course : courses) {
            int unitCount = unitMap.getOrDefault(course.getId(), 0);
            int assignmentCount = assignmentMap.getOrDefault(course.getId(), 0);
            dtoList.add(CourseListResponse.create(course, unitCount, assignmentCount));
        }
        return new CourseCountListResponse(dtoList.size(), dtoList);
    }
}
