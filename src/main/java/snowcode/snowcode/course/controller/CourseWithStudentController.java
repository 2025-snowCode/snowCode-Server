package snowcode.snowcode.course.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.service.AuthContext;
import snowcode.snowcode.auth.service.MemberService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.service.CourseWithEnrollmentFacade;
import snowcode.snowcode.student.dto.StudentProgressListResponse;
import snowcode.snowcode.student.dto.StudentRequest;
import snowcode.snowcode.student.dto.StudentResponse;
import snowcode.snowcode.unit.service.UnitProgressFacade;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseWithStudentController {

    private final CourseWithEnrollmentFacade courseWithEnrollmentFacade;
    private final UnitProgressFacade unitProgressFacade;
    private final MemberService memberService;
    private final AuthContext authContext;

    @PostMapping("/{courseId}/enrollments")
    @Operation(summary = "학생 등록 API", description = "학생 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학생 등록 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_INPUT",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "404", description = "학생을 찾을 수 없습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "404", description = "강의가 존재하지 않습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<String> addStudent(@PathVariable Long courseId, @Valid @RequestBody StudentRequest dto) {
        authContext.isCourseOwner(courseId); // 인가
        courseWithEnrollmentFacade.addStudentWithEnroll(courseId, dto);
        return ResponseUtil.success("학생 추가에 성공하였습니다.");
    }

    @GetMapping("/{courseId}/enrollments")
    @Operation(summary = "학생 전체 조회 API", description = "수강 중인 학생 전체 조회 (검색-필터링 가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학생 전체 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentProgressListResponse.class))}),

    })
    public BasicResponse<StudentProgressListResponse> findAllStudentWithStatus(
            @PathVariable Long courseId,
            @RequestParam(required = false) String studentId) {

        List<Member> members = memberService.findNonAdmin(courseId, studentId);
        StudentProgressListResponse students = unitProgressFacade.findAllStudents(members, courseId);
        return ResponseUtil.success(students);
    }

    @GetMapping("/{courseId}/enrollments/{memberId}")
    @Operation(summary = "학생 조회 API", description = "학생 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학생 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class))}),

    })
    public BasicResponse<StudentResponse> findStudentWithStatus(@PathVariable Long courseId, @PathVariable Long memberId) {
        authContext.isCourseOwner(courseId); // 인가
        StudentResponse student = unitProgressFacade.findStudentsWithCourse(memberId, courseId);
        return ResponseUtil.success(student);
    }

    @DeleteMapping("/{courseId}/enrollments/{memberId}")
    @Operation(summary = "학생 삭제 API", description = "수강 중인 학생 삭제 (멤버 자체는 삭제 X)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학생 삭제 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "수강 정보가 존재하지 않습니다. (학생이 수강에 등록되어 있지 않은 경우)",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<String> deleteStudentWithEnrollment(@PathVariable Long courseId, @PathVariable Long memberId) {
        authContext.isCourseOwner(courseId); // 인가
        courseWithEnrollmentFacade.deleteStudentWithEnrollment(courseId, memberId);
        return ResponseUtil.success("학생 삭제에 성공하였습니다.");
    }
}
