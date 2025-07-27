package snowcode.snowcode.unit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.service.CourseService;
import snowcode.snowcode.unit.dto.UnitCountListResponse;
import snowcode.snowcode.unit.dto.UnitRequest;
import snowcode.snowcode.unit.dto.UnitResponse;
import snowcode.snowcode.unit.service.UnitService;
import snowcode.snowcode.unit.service.UnitWithAssignmentFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitService;
    private final CourseService courseService;
    private final UnitWithAssignmentFacade unitWithAssignmentFacade;

    @PostMapping("/{courseId}")
    public BasicResponse<UnitResponse> createUnit(@PathVariable Long courseId, @Valid @RequestBody UnitRequest dto) {
        Course course = courseService.findCourse(courseId);
        UnitResponse unit = unitService.createUnit(course, dto);
        return ResponseUtil.success(unit);
    }

    @GetMapping("/{unitId}")
    public BasicResponse<UnitResponse> findUnit(@PathVariable Long unitId) {
        UnitResponse unit = unitService.findById(unitId);
        return ResponseUtil.success(unit);
    }

    @PutMapping("/{unitId}")
    public BasicResponse<UnitResponse> updateUnit(@PathVariable Long unitId, @Valid @RequestBody UnitRequest dto) {
        UnitResponse unit = unitService.updateUnit(unitId, dto);
        return ResponseUtil.success(unit);
    }

    @DeleteMapping("/{unitId}")
    public BasicResponse<String> deleteUnit (@PathVariable Long unitId) {
        unitWithAssignmentFacade.deleteAllByUnitId(unitId);
        return ResponseUtil.success("단원 삭제에 성공하였습니다.");
    }
}
