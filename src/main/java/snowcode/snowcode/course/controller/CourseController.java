package snowcode.snowcode.course.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;
import snowcode.snowcode.auth.service.MemberService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.dto.*;
import snowcode.snowcode.course.service.CourseWithEnrollmentFacade;
import snowcode.snowcode.course.service.CourseService;
import snowcode.snowcode.course.service.CourseWithMemberFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final CourseWithEnrollmentFacade courseWithEnrollmentFacade;
    private final MemberService memberService;
    private final CourseWithMemberFacade courseWithMemberFacade;

    @PostMapping("/{memberId}")
    public BasicResponse<CourseResponse> createCourse(@PathVariable Long memberId, @Valid @RequestBody CourseRequest dto) {
        Member member = memberService.findMember(memberId);
        CourseResponse course = courseWithEnrollmentFacade.createCourseWithEnroll(member, dto);
        return ResponseUtil.success(course);
    }

    @PutMapping("/{id}")
    public BasicResponse<CourseResponse> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequest dto) {
        CourseResponse course = courseService.updateCourse(id, dto);
        return ResponseUtil.success(course);
    }

    @DeleteMapping("/{id}")
    public BasicResponse<String> deleteCourse(@PathVariable Long id) {
        courseWithEnrollmentFacade.deleteCourseAndEnrollment(id);
        return ResponseUtil.success("강의 삭제에 성공하였습니다.");
    }

    @GetMapping("/{memberId}/my")
    public BasicResponse<CourseCountListResponse> findMyCourses(@PathVariable Long memberId) {
        CourseCountListResponse myCourses = courseWithEnrollmentFacade.findMyCourses(memberId);
        return ResponseUtil.success(myCourses);
    }

    @GetMapping("/{memberId}/{courseId}")
    public BasicResponse<?> findCourse(@PathVariable Long memberId, @PathVariable Long courseId) {
        Member member = memberService.findMember(memberId);
        if (member.getRole().equals(Role.ADMIN)) {
            CourseDetailAdminResponse course = courseWithMemberFacade.createAdminCourseResponse(courseId);
            return ResponseUtil.success(course);
        } else {
            CourseDetailStudentResponse course = courseWithMemberFacade.createStudentCourseResponse(memberId, courseId);
            return ResponseUtil.success(course);
        }
    }
}
