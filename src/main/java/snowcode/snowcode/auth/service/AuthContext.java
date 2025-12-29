package snowcode.snowcode.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.service.AssignmentService;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;
import snowcode.snowcode.auth.exception.AuthErrorCode;
import snowcode.snowcode.auth.exception.AuthException;
import snowcode.snowcode.code.domain.Code;
import snowcode.snowcode.code.service.CodeService;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.service.CourseService;
import snowcode.snowcode.enrollment.domain.Enrollment;
import snowcode.snowcode.enrollment.service.EnrollmentService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthContext {
    private final AuthService authService;
    private final AssignmentService assignmentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final CodeService codeService;

    public void isAdmin() {
        // 인가
        if (!authService.getRole().equals(Role.ADMIN)) {
            throw new AuthException(AuthErrorCode.INVALID_ROLE);
        }
    }

    public void isCourseOwner(Long courseId) {
        Long memberId = authService.loadMember().getId();
        Course course = courseService.findCourse(courseId);
        // enrollment에서 찾아야 함!
        if (!memberId.equals(course.getCreatedBy())) {
            throw new AuthException(AuthErrorCode.INVALID_COURSE_ROLE);
        }
    }

    public void isAssignmentOwner(Long assignmentId) {
        Long memberId = authService.loadMember().getId();
        Long createdBy = assignmentService.findById(assignmentId).getCreatedBy();

        if (!memberId.equals(createdBy)) throw new AuthException(AuthErrorCode.INVALID_ASSIGNMENT_ROLE);
    }

    public void isEnrolledInCourse(Long courseId) {
        Long memberId = authService.loadMember().getId();
        List<Enrollment> enrollments = enrollmentService.findByCourseId(courseId);
        boolean isEnrolled = false;
        for (Enrollment e : enrollments) {
            if (memberId.equals(e.getMember().getId())) {
                isEnrolled = true;
                break;
            }
        }
        if (!isEnrolled) {
            throw new AuthException(AuthErrorCode.INVALID_ENROLLED_ROLE);
        }
    }

    public void isFindCode(Long codeId) {
        Code code = codeService.findById(codeId);
        Long createdCodeBy = code.getSubmission().getMember().getId();
        Member member = authService.loadMember();
        if (!createdCodeBy.equals(member.getId()) && !member.getRole().equals(Role.ADMIN)) {
            throw new AuthException(AuthErrorCode.INVALID_CODE_ROLE);
        }
    }
}
