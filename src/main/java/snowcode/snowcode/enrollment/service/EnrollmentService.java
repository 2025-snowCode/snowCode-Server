package snowcode.snowcode.enrollment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.enrollment.domain.Enrollment;
import snowcode.snowcode.enrollment.domain.EnrollmentStatus;
import snowcode.snowcode.enrollment.exception.EnrollmentErrorCode;
import snowcode.snowcode.enrollment.exception.EnrollmentException;
import snowcode.snowcode.enrollment.repository.EnrollmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public void createEnrollment(Member member, Course course) {
        Enrollment enrollment = Enrollment.createEnrollment(0, 0, EnrollmentStatus.ENROLLED, member, course);
        enrollmentRepository.save(enrollment);
    }

    public void deleteEnrollmentWithCourseId(Long courseId) {
        enrollmentRepository.deleteByCourseId(courseId);
    }

    @Transactional(readOnly = true)
    public Enrollment findById(Long id) {
        return enrollmentRepository.findById(id).orElseThrow(
                () -> new EnrollmentException(EnrollmentErrorCode.ENROLLMENT_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public List<Enrollment> findByMemberId(Long memberId) {
        return enrollmentRepository.findAllByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public List<Course> findCoursesByEnrollment(List<Enrollment> enrollments) {
        return enrollments.stream()
                .map(Enrollment::getCourse)
                .toList();
    }
}
