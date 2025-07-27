package snowcode.snowcode.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.dto.CourseCountListResponse;
import snowcode.snowcode.course.dto.CourseListResponse;
import snowcode.snowcode.course.dto.CourseRequest;
import snowcode.snowcode.course.dto.CourseResponse;
import snowcode.snowcode.enrollment.domain.Enrollment;
import snowcode.snowcode.enrollment.service.EnrollmentService;
import snowcode.snowcode.student.service.StudentService;
import snowcode.snowcode.unit.service.UnitService;
import snowcode.snowcode.unit.service.UnitWithAssignmentFacade;

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
    private final UnitWithAssignmentFacade unitWithAssignmentFacade;

    public CourseResponse createCourseWithEnroll(Member member, CourseRequest dto) {
        Course course = courseService.createCourse(dto);
        List<Member> members = studentService.findStudents(dto.students());
        studentService.addAdminInMembers(member, members);
        enrollmentService.createEnrollment(members, course);
        return CourseResponse.from(course);
    }

    public void deleteCourseAndEnrollment(Long courseId) {
        List<Long> unitIds = unitService.findIdsByCourseId(courseId);
        registrationService.deleteAllByUnitIdIn(unitIds);
        unitService.deleteAllById(unitIds);
        enrollmentService.deleteEnrollmentWithCourseId(courseId);
        courseService.deleteCourse(courseId);
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

    @Transactional(readOnly = true)
    public List<Member> findNonAdminByCourseId(Long courseId) {
        List<Enrollment> enrollmentList = enrollmentService.findByCourseId(courseId);
        List<Member> members = new ArrayList<>();

        for (Enrollment e : enrollmentList) {
            Member member = e.getMember();
            if (!member.getRole().equals(Role.ADMIN)) members.add(member);
        }
        return members;
    }
}
