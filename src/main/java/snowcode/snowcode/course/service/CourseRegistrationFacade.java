package snowcode.snowcode.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.service.AssignmentService;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.dto.CourseCountListResponse;
import snowcode.snowcode.course.dto.CourseListResponse;
import snowcode.snowcode.course.dto.CourseRequest;
import snowcode.snowcode.course.dto.CourseResponse;
import snowcode.snowcode.enrollment.domain.Enrollment;
import snowcode.snowcode.enrollment.service.EnrollmentService;
import snowcode.snowcode.unit.service.UnitService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseRegistrationFacade {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final UnitService unitService;
    private final AssignmentService assignmentService;

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


    @Transactional(readOnly = true)
    public CourseCountListResponse findMyCourses(Long memberId) {
        List<Enrollment> enrollmentList = enrollmentService.findByMemberId(memberId);
        List<Course> courses = enrollmentService.findCoursesByEnrollment(enrollmentList);
        List<Long> courseIds = courseService.extractCourseIds(courses);

        Map<Long, Integer> unitMap = unitService.countUnitsByCourseId(courseIds);
        Map<Long, Integer> assignmentMap = assignmentService.countAssignmentsByCourseId(courseIds);

        List<CourseListResponse> dtoList = new ArrayList<>();
        for (Course course : courses) {
            int unitCount = unitMap.getOrDefault(course.getId(), 0);
            int assignmentCount = assignmentMap.getOrDefault(course.getId(), 0);
            dtoList.add(CourseListResponse.create(course, unitCount, assignmentCount));
        }
        return new CourseCountListResponse(dtoList.size(), dtoList);
    }
}
