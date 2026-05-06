package snowcode.snowcode.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.exception.AuthErrorCode;
import snowcode.snowcode.auth.exception.AuthException;
import snowcode.snowcode.chatRoom.service.ChatRoomFacade;
import snowcode.snowcode.chatRoom.service.ChatRoomService;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final ChatRoomService chatRoomService;
    private final ChatRoomFacade chatRoomFacade;

    public CourseResponse createCourseWithEnroll(Member admin, CourseRequest dto) {
        // 강의-수강-학생 등록
        Course course = courseService.createCourse(admin.getId(), dto);
        List<Member> students = studentService.findStudents(dto.students());
        studentService.addAdminInMembers(admin, students);
        enrollmentService.createEnrollment(students, course);

        // 채팅방 생성
        for (Member student : students) {
            // 채팅방 하나 생성
            // student, member 각각 채팅 참여자 생성 // FIXME - 동시성 이슈
            Long adminId = admin.getId();
            Long studentId = student.getId();
            if (adminId != studentId && chatRoomService.isNotPresentChatRoom(adminId, studentId)) {
                chatRoomFacade.createChatRoom(admin, student);
            }
        }
        return CourseResponse.from(course);
    }

    public void addStudentWithEnroll(Member admin, Long courseId, StudentRequest dto) {
        Member student = studentService.findByStudentId(dto.studentId());
        Course course = courseService.findCourse(courseId);
        boolean isAlreadyEnrolled = enrollmentService.isAlreadyEnrolled(courseId, student.getId());

        if (isAlreadyEnrolled) throw new AuthException(AuthErrorCode.IS_ALREADY_ENROLLED_STUDENT);
        enrollmentService.createEnrollment(student, course);

        // 학생 추가 시 채팅방도 추가
        Long adminId = admin.getId();
        Long studentId = student.getId();
        if (adminId != studentId && chatRoomService.isNotPresentChatRoom(adminId, studentId)) {
            chatRoomFacade.createChatRoom(admin, student);
        }
    }

    public void addStudentWithEnroll(Member admin, Long courseId, MultipartFile file) {

        List<StudentRequest> requests;
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(file.getInputStream())
            );

            requests = br.lines()
                    .map(line -> line.replace("\uFEFF", "").trim())
                    .filter(line -> !line.isBlank())
                    .flatMap(line -> Arrays.stream(line.split(",")))
                    .map(String::trim)
                    .filter(value -> !value.isBlank())
                    .map(StudentRequest::new)
                    .toList();
        } catch (Exception e) {
            throw new AuthException(AuthErrorCode.FAILED_UPLOAD_STUDENT_CSV);
        }
        if (requests.isEmpty()) return;

        for (StudentRequest rq : requests) {
            addStudentWithEnroll(admin, courseId, rq);
        }
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

    @Transactional(readOnly = true)
    public CourseCountListResponse findMyCourses(Long memberId, String title) {
        List<Enrollment> enrollmentList = enrollmentService.findByMemberIdAndTitle(memberId, title);
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
