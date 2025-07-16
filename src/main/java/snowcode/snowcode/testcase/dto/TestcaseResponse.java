package snowcode.snowcode.testcase.dto;

import snowcode.snowcode.testcase.domain.Testcase;

public record TestcaseResponse(Long id, String testcase, String answer, String role, boolean isPublic) {

    public static TestcaseResponse from (Testcase testcase) {
        return new TestcaseResponse(testcase.getId(), testcase.getTestcase(), testcase.getAnswer(), testcase.getRole().toString(), testcase.isPublic());
    }
}
