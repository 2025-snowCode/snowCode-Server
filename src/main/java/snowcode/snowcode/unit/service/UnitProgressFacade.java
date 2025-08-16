package snowcode.snowcode.unit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.service.AssignmentService;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.service.CourseService;
import snowcode.snowcode.student.dto.StudentProgressListResponse;
import snowcode.snowcode.student.dto.StudentProgressResponse;
import snowcode.snowcode.submission.domain.Submission;
import snowcode.snowcode.submission.service.SubmissionService;
import snowcode.snowcode.unit.domain.Unit;
import snowcode.snowcode.unit.domain.UnitAssignmentStatus;
import snowcode.snowcode.unit.dto.UnitProgressResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitProgressFacade {

    private final UnitService unitService;
    private final AssignmentService assignmentService;
    private final RegistrationService registrationService;
    private final SubmissionService submissionService;
    private final CourseService courseService;

    public StudentProgressListResponse findAllStudents(List<Member> members, Long courseId) {
        boolean isSubmitted = false;
        int totalScore = 0;
        int score = 0;
        Course course = courseService.findCourse(courseId);
        List<Unit> units = unitService.findAllByCourseId(courseId);

        // map 형태로.

        // totalScore 계산
        for (Unit unit : units) {
            List<Assignment> assignments = registrationService.findAllByUnitId(unit.getId()); // FIXME - 한 번에 쿼리 날리기
            for (Assignment assignment : assignments) {
                totalScore += assignment.getScore();
            }
        }

        List<StudentProgressResponse> students = new ArrayList<>();
        for (Member member : members) {
            List<UnitProgressResponse> progress = new ArrayList<>();
            for (Unit unit : units) {
                List<Assignment> assignments = registrationService.findAllByUnitId(unit.getId()); // FIXME - 한 번에 쿼리 날리기
                isSubmitted = false;
                for (Assignment assignment : assignments) {
                    Optional<Submission> submitted = submissionService.isSubmitted(member.getId(), assignment);
                    if (submitted.isPresent()) {
                        score += submitted.get().getScore();
                        isSubmitted = true;
                    }
                }
                if (!isSubmitted) {
                    progress.add(new UnitProgressResponse(UnitAssignmentStatus.NOT_SUBMITTED.toString()));
                } else if (totalScore == score) {
                    progress.add(new UnitProgressResponse(UnitAssignmentStatus.PASSED.toString()));
                } else if (totalScore > score) {
                    if (score != 0) progress.add(new UnitProgressResponse(UnitAssignmentStatus.PARTIAL.toString()));
                    else progress.add(new UnitProgressResponse(UnitAssignmentStatus.FAILED.toString()));
                }
                students.add(StudentProgressResponse.of(member, score, totalScore, progress));
            }
        }
        return StudentProgressListResponse.of(course, units.size(), students);
        // boolean isSubmitted <판별용> -> false
        // 1. 강의 id로 모든 단원 조회
        // 2. 단원마다 모든 과제 조회

        // 3. 단원 하나의 과제 하나씩 순회 돌기
            // 3-0. isSubmitted == false
            // 3-1. score 가져오기 => totalScore 에 더하기
            // 3-2. Submission 테이블에서 score 찾기 ///////////////////
                // 3-3. 존재하지 않으면 continue
                // 3-4. 존재하면 newScore 에 더하기 (Member, 등록된 과제로 검색), isSubmitted = true 로 바꾸기
        // 4. totalScore vs newScore 비교
            // 4-0. isSubmitted == false 이면 NOT_SUBMITTED
            // 4-1. totalScore == newScore 이면 PASSED
            // 4-2. totalScore > newScore 이면
                // 4-3. newScore != 0이면 PARTIAL
                // 4-4. newScore == 0이면 FAILED
    }
}
