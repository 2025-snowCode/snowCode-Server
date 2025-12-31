package snowcode.snowcode.unit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.auth.service.AuthContext;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.service.CourseService;
import snowcode.snowcode.unit.domain.Unit;
import snowcode.snowcode.unit.dto.UnitRequest;
import snowcode.snowcode.unit.dto.UnitResponse;
import snowcode.snowcode.unit.dto.UnitUpdateRequest;
import snowcode.snowcode.unit.dto.UnitWithAssignmentResponse;
import snowcode.snowcode.unit.service.UnitService;
import snowcode.snowcode.unit.service.UnitWithAssignmentFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/units")
@Tag(name = "단원", description = "Unit API")
public class UnitController {

    private final UnitService unitService;
    private final CourseService courseService;
    private final UnitWithAssignmentFacade unitWithAssignmentFacade;
    private final RegistrationService registrationService;
    private final AuthContext authContext;

    @PostMapping("/{courseId}")
    @Operation(summary = "단원 추가 API", description = "단원 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단원 추가 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UnitWithAssignmentResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_INPUT",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "400", description = "유효한 날짜가 아닙니다. (yyyy-MM-dd 형식으로 넣어야 함)",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<UnitWithAssignmentResponse> createUnit(@PathVariable Long courseId, @Valid @RequestBody UnitRequest dto) {
        authContext.isCourseOwner(courseId); // 인가
        Course course = courseService.findCourse(courseId);
        UnitWithAssignmentResponse unitList = unitWithAssignmentFacade.registrationAssignment(course, dto);
        return ResponseUtil.success(unitList);
    }

    @GetMapping("/{unitId}")
    @Operation(summary = "단원 조회 API", description = "단원 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단원 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UnitWithAssignmentResponse.class))}),
            @ApiResponse(responseCode = "404", description = "단원이 존재하지 않습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<UnitWithAssignmentResponse> findUnit(@PathVariable Long unitId) {
        UnitWithAssignmentResponse unit = unitWithAssignmentFacade.findAllByUnitId(unitId);
        return ResponseUtil.success(unit);
    }

    @PutMapping("/{unitId}")
    @Operation(summary = "단원 수정 API", description = "단원 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단원 수정 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UnitResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_INPUT",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "404", description = "단원이 존재하지 않습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<UnitResponse> updateUnit(@PathVariable Long unitId, @Valid @RequestBody UnitUpdateRequest dto) {
        // TODO: 인가 파트 최적화 필요
        Unit unit = unitService.findUnit(unitId);
        Long courseId = unit.getCourse().getId();
        authContext.isCourseOwner(courseId); // 인가

        UnitResponse unitDto = unitService.updateUnit(unitId, dto);
        return ResponseUtil.success(unitDto);
    }

    @DeleteMapping("/{unitId}")
    @Operation(summary = "단원 삭제 API", description = "단원 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단원 삭제 성공 (아직 예외 처리 전,,)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "단원이 존재하지 않습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<String> deleteUnit (@PathVariable Long unitId) {
        Unit unit = unitService.findUnit(unitId);
        Long courseId = unit.getCourse().getId();
        authContext.isCourseOwner(courseId); // 인가

        unitWithAssignmentFacade.deleteAllByUnitId(unitId);
        return ResponseUtil.success("단원 삭제에 성공하였습니다.");
    }

    @DeleteMapping("/{unitId}/assignments/{assignmentId}")
    @Operation(summary = "등록된 과제 삭제 API", description = "등록된 과제 삭제 (단원과 과제 삭제는 없이, 등록된 과제와 단원의 연결을 끊음)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록된 과제 삭제 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "단원이 존재하지 않습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<String> deleteRegistrationAssignment(@PathVariable Long unitId, @PathVariable Long assignmentId) {
        Unit unit = unitService.findUnit(unitId);
        Long courseId = unit.getCourse().getId();
        authContext.isCourseOwner(courseId); // 인가

        registrationService.deleteByUnitIdAndAssignmentId(unitId, assignmentId);
        return ResponseUtil.success("등록된 과제 삭제에 성공하였습니다.");
    }
}
