package snowcode.snowcode.course.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.service.MemberService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.dto.CourseRequest;
import snowcode.snowcode.course.dto.CourseResponse;
import snowcode.snowcode.course.service.CourseRegistrationFacade;
import snowcode.snowcode.course.service.CourseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final CourseRegistrationFacade courseRegistrationFacade;
    private final MemberService memberService;

    @PostMapping("/{memberId}")
    public BasicResponse<CourseResponse> createCourse(@PathVariable Long memberId, @Valid @RequestBody CourseRequest dto) {
        Member member = memberService.findMember(memberId);
        CourseResponse course = courseRegistrationFacade.createCourseWithEnroll(member, dto);
        return ResponseUtil.success(course);
    }

    @PutMapping("/{id}")
    public BasicResponse<CourseResponse> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequest dto) {
        CourseResponse course = courseService.updateCourse(id, dto);
        return ResponseUtil.success(course);
    }

    @DeleteMapping("/{id}")
    public BasicResponse<String> deleteCourse(@PathVariable Long id) {
        courseRegistrationFacade.deleteCourseAndEnrollment(id);
        return ResponseUtil.success("강의 삭제에 성공하였습니다.");
    }
}
