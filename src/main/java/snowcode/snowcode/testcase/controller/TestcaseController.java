package snowcode.snowcode.testcase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.service.AssignmentService;
import snowcode.snowcode.auth.service.AuthContext;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.testcase.domain.Testcase;
import snowcode.snowcode.testcase.dto.TestcaseRequest;
import snowcode.snowcode.testcase.dto.TestcaseResponse;
import snowcode.snowcode.testcase.service.TestcaseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/testcase")
public class TestcaseController {

    private final TestcaseService testcaseService;
    private final AssignmentService assignmentService;
    private final AuthContext authContext;

    @PostMapping("{assignmentId}")
    @Operation(summary = "테스트케이스 추가 API", description = "테스트케이스 추가 (사용 X)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "테스트케이스 추가 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TestcaseResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_INPUT",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "404", description = "과제가 존재하지 않습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<TestcaseResponse> createTestcase(@PathVariable Long assignmentId, @Valid @RequestBody TestcaseRequest dto) {
        authContext.isAssignmentOwner(assignmentId); // 인가
        Assignment assignment = assignmentService.findById(assignmentId);
        TestcaseResponse testcase = testcaseService.createTestcase(assignment, dto);
        return ResponseUtil.success(testcase);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "테스트케이스 삭제 API", description = "사용 여부 불확실함")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "테스트케이스 삭제 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "테스트케이스가 존재하지 않습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<String> deleteTestcase(@PathVariable Long id) {
        Testcase testcase = testcaseService.findById(id);
        Long assignmentId = testcase.getAssignment().getId();
        authContext.isAssignmentOwner(assignmentId); // 인가

        testcaseService.deleteTestcase(testcase);
        return ResponseUtil.success("테스트케이스 삭제에 성공하였습니다.");
    }
}
