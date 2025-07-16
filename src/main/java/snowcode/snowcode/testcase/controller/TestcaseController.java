package snowcode.snowcode.testcase.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public BasicResponse<TestcaseResponse> createTestcase(@Valid @RequestBody TestcaseRequest dto) {
        TestcaseResponse testcase = testcaseService.createTestcase(dto);
        return ResponseUtil.success(testcase);
    }

    @DeleteMapping("{id}")
    public BasicResponse<String> deleteTestcase(@PathVariable Long id) {
        testcaseService.deleteTestcase(id);
        return ResponseUtil.success("테스트케이스 삭제에 성공하였습니다.");
    }
}
