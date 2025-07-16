package snowcode.snowcode.testcase.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public BasicResponse<TestcaseResponse> createTestcase(@RequestBody TestcaseRequest dto) {
        TestcaseResponse testcase = testcaseService.createTestcase(dto);
        return ResponseUtil.success(testcase);
    }
}
