package snowcode.snowcode.course.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.dto.CourseCreateRequest;
import snowcode.snowcode.course.dto.CourseResponse;
import snowcode.snowcode.course.service.CourseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public BasicResponse<CourseResponse> createCourse(@Valid @RequestBody CourseCreateRequest dto) {
        CourseResponse course = courseService.createCourse(dto);
        return ResponseUtil.success(course);
    }
}
