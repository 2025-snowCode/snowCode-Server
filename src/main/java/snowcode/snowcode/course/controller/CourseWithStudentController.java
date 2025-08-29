package snowcode.snowcode.course.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.service.MemberService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.service.CourseWithEnrollmentFacade;
import snowcode.snowcode.student.dto.StudentProgressListResponse;
import snowcode.snowcode.student.dto.StudentRequest;
import snowcode.snowcode.student.dto.StudentResponse;
import snowcode.snowcode.unit.service.UnitProgressFacade;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseWithStudentController {

    private final CourseWithEnrollmentFacade courseWithEnrollmentFacade;
    private final UnitProgressFacade unitProgressFacade;
    private final MemberService memberService;

    @PostMapping("/{courseId}/enrollments")
    public BasicResponse<String> addStudent(@PathVariable Long courseId, @Valid @RequestBody StudentRequest dto) {
        courseWithEnrollmentFacade.addStudentWithEnroll(courseId, dto);
        return ResponseUtil.success("학생 추가에 성공하였습니다.");
    }

    @GetMapping("/{courseId}/enrollments")
    public BasicResponse<StudentProgressListResponse> findAllStudentWithStatus(
            @PathVariable Long courseId,
            @RequestParam(required = false) String studentId) {

        List<Member> members = memberService.findNonAdmin(courseId, studentId);
        StudentProgressListResponse students = unitProgressFacade.findAllStudents(members, courseId);
        return ResponseUtil.success(students);
    }

    @GetMapping("/{courseId}/enrollments/{memberId}")
    public BasicResponse<StudentResponse> findStudentWithStatus(@PathVariable Long courseId, @PathVariable Long memberId) {
        StudentResponse student = unitProgressFacade.findStudentsWithCourse(memberId, courseId);
        return ResponseUtil.success(student);
    }

    @DeleteMapping("/{courseId}/enrollments/{memberId}")
    public BasicResponse<String> deleteStudentWithEnrollment(@PathVariable Long courseId, @PathVariable Long memberId) {
        courseWithEnrollmentFacade.deleteStudentWithEnrollment(courseId, memberId);
        return ResponseUtil.success("학생 삭제에 성공하였습니다.");
    }
}
