package snowcode.snowcode.testcase.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.service.AssignmentService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.testcase.dto.TestcaseRequest;
import snowcode.snowcode.testcase.dto.TestcaseResponse;
import snowcode.snowcode.testcase.service.TestcaseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/testcase")
public class TestcaseController {

    private final TestcaseService testcaseService;
    private final AssignmentService assignmentService;

    @PostMapping("{assignmentId}")
    public BasicResponse<TestcaseResponse> createTestcase(@PathVariable Long assignmentId, @Valid @RequestBody TestcaseRequest dto) {
        Assignment assignment = assignmentService.findById(assignmentId);
        TestcaseResponse testcase = testcaseService.createTestcase(assignment, dto);
        return ResponseUtil.success(testcase);
    }

    @DeleteMapping("{id}")
    public BasicResponse<String> deleteTestcase(@PathVariable Long id) {
        testcaseService.deleteTestcase(id);
        return ResponseUtil.success("테스트케이스 삭제에 성공하였습니다.");
    }
}
