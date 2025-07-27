package snowcode.snowcode.unit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.service.CourseService;
import snowcode.snowcode.unit.dto.UnitRequest;
import snowcode.snowcode.unit.dto.UnitResponse;
import snowcode.snowcode.unit.dto.UnitWithAssignmentResponse;
import snowcode.snowcode.unit.service.UnitService;
import snowcode.snowcode.unit.service.UnitWithAssignmentFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitService;
    private final CourseService courseService;
    private final UnitWithAssignmentFacade unitWithAssignmentFacade;
    private final RegistrationService registrationService;

    @PostMapping("/{courseId}")
    public BasicResponse<UnitWithAssignmentResponse> createUnit(@PathVariable Long courseId, @Valid @RequestBody UnitRequest dto) {
        Course course = courseService.findCourse(courseId);
        UnitWithAssignmentResponse unitList = unitWithAssignmentFacade.registrationAssignment(course, dto);
        return ResponseUtil.success(unitList);
    }

    @GetMapping("/{unitId}")
    public BasicResponse<UnitWithAssignmentResponse> findUnit(@PathVariable Long unitId) {
        UnitWithAssignmentResponse unit = unitWithAssignmentFacade.findAllByUnitId(unitId);
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

    @DeleteMapping("/{unitId}/assignments/{assignmentId}")
    public BasicResponse<String> deleteRegistrationAssignment(@PathVariable Long unitId, @PathVariable Long assignmentId) {
        registrationService.deleteByUnitIdAndAssignmentId(unitId, assignmentId);
        return ResponseUtil.success("등록된 과제 삭제에 성공하였습니다.");
    }
}
