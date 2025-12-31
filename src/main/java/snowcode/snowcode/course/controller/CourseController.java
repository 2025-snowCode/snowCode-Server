package snowcode.snowcode.course.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;
import snowcode.snowcode.auth.service.AuthContext;
import snowcode.snowcode.auth.service.AuthService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.dto.*;
import snowcode.snowcode.course.service.CourseService;
import snowcode.snowcode.course.service.CourseWithEnrollmentFacade;
import snowcode.snowcode.course.service.CourseWithMemberFacade;
import snowcode.snowcode.course.service.CourseWithRegistrationFacade;
import snowcode.snowcode.unit.dto.UnitCountListResponse;
import snowcode.snowcode.unit.dto.UnitWithAssignmentResponse;
import snowcode.snowcode.unit.service.UnitWithAssignmentFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
@Tag(name = "강의", description = "Course API")
public class CourseController {
    private final CourseService courseService;
    private final CourseWithEnrollmentFacade courseWithEnrollmentFacade;
    private final CourseWithMemberFacade courseWithMemberFacade;
    private final CourseWithRegistrationFacade courseWithRegistrationFacade;
    private final UnitWithAssignmentFacade unitWithAssignmentFacade;
    private final AuthService authService;
    private final AuthContext authContext;

    @PostMapping
    @Operation(summary = "강의 추가 API", description = "강의 추가 (학생까지 추가)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의 추가 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_INPUT",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "400", description = "연도 입력이 잘못되었습니다. (2000 < year < 2100)",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "400", description = "학기 입력이 잘못되었습니다. (FIRST, SECOND, SUMMER, WINTER 중 하나)",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),

    })
    public BasicResponse<CourseResponse> createCourse(@Valid @RequestBody CourseRequest dto) {
        // 인가
        authContext.isAdmin();

        Member member = authService.loadMember();
        CourseResponse course = courseWithEnrollmentFacade.createCourseWithEnroll(member, dto);
        return ResponseUtil.success(course);
    }

    @GetMapping("/{courseId}/assignments")
    @Operation(summary = "강의별 전체 과제 조회 API", description = "title이 같은 강의별 전체 과제 조회 (분반, 학기 등 상관 없음)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의별 전체 과제 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CourseCountWithAssignmentResponse.class))}),
            @ApiResponse(responseCode = "404", description = "강의가 존재하지 않습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<CourseCountWithAssignmentResponse> findAllAssignmentWithCourseTitle(@PathVariable Long courseId) {
        Long memberId = authService.loadMember().getId();
        CourseCountWithAssignmentResponse courseList = courseWithRegistrationFacade.findCourseTitleWithAssignments(memberId, courseId);
        return ResponseUtil.success(courseList);
    }

    @PutMapping("/{courseId}")
    @Operation(summary = "강의 수정 API", description = "강의 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의 수정 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_INPUT",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "404", description = "강의가 존재하지 않습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "400", description = "연도 입력이 잘못되었습니다 (2000 < year < 2100)",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "400", description = "학기 입력이 잘못되었습니다. (FIRST, SECOND, SUMMER, WINTER 중 하나)",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<CourseResponse> updateCourse(@PathVariable Long courseId, @Valid @RequestBody CourseRequest dto) {
        authContext.isCourseOwner(courseId); // 인가
        CourseResponse course = courseService.updateCourse(courseId, dto);
        return ResponseUtil.success(course);
    }

    @DeleteMapping("/{courseId}")
    @Operation(summary = "강의 삭제 API", description = "강의 삭제 (관련 단원 함께 삭제, 연결된 과제는 삭제 X)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의 삭제 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "강의가 존재하지 않습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<String> deleteCourse(@PathVariable Long courseId) {
        authContext.isCourseOwner(courseId); // 인가
        courseWithEnrollmentFacade.deleteCourseAndEnrollment(courseId);
        return ResponseUtil.success("강의 삭제에 성공하였습니다.");
    }

    @GetMapping("/my")
    @Operation(summary = "전체 강의 조회 API", description = "내가 듣는 전체 강의 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 강의 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CourseCountListResponse.class))}),
            })
    public BasicResponse<CourseCountListResponse> findMyCourses() {
        Long memberId = authService.loadMember().getId();
        CourseCountListResponse myCourses = courseWithEnrollmentFacade.findMyCourses(memberId);
        return ResponseUtil.success(myCourses);
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "강의 조회 API", description = "단건 강의 조회 (학생/관리자에 따라 리턴값 다름)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단건 강의 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(
                            oneOf = { CourseDetailAdminResponse.class, CourseDetailStudentResponse.class }
                    ))}),
            @ApiResponse(responseCode = "404", description = "강의가 존재하지 않습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<?> findCourse(@PathVariable Long courseId) {

        Member member = authService.loadMember();
        if (member.getRole().equals(Role.ADMIN)) {
            CourseDetailAdminResponse course = courseWithMemberFacade.createAdminCourseResponse(courseId);
            return ResponseUtil.success(course);
        } else {
            CourseDetailStudentResponse course = courseWithMemberFacade.createStudentCourseResponse(member.getId(), courseId);
            return ResponseUtil.success(course);
        }
    }

    @GetMapping("/{courseId}/units")
    @Operation(summary = "전체 단원 조회 API", description = "전체 단원 조회 (이름)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 단원 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UnitWithAssignmentResponse.class))}),
            @ApiResponse(responseCode = "404", description = "단원이 존재하지 않습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<UnitCountListResponse> findAllUnit(@PathVariable Long courseId) {
        authContext.isCourseOwner(courseId); // 인가
        UnitCountListResponse units = unitWithAssignmentFacade.findAllUnit(courseId);
        return ResponseUtil.success(units);
    }
}
