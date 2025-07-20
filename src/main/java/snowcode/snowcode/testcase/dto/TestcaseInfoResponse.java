package snowcode.snowcode.testcase.dto;

import snowcode.snowcode.testcase.domain.Testcase;

public record TestcaseInfoResponse(Long id, String input, String output) {

    public static TestcaseInfoResponse of(Testcase testcase) {
        return new TestcaseInfoResponse(testcase.getId(), testcase.getTestcase(), testcase.getAnswer());
    }
}
