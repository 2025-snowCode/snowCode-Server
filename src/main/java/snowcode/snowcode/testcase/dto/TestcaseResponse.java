package snowcode.snowcode.testcase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.testcase.domain.Testcase;

public record TestcaseResponse(
        @Schema(description = "테스트케이스 id", example = "1")
        Long id,
        @Schema(description = "테스트케이스 문제", example = "1+2")
        String testcase,
        @Schema(description = "테스트케이스 정답", example = "3")
        String answer,
        @Schema(description = "테스트케이스/예제", example = "TESTCASE")
        String role,
        @Schema(description = "공개 여부", example = "false")
        boolean isPublic) {

    public static TestcaseResponse from (Testcase testcase) {
        return new TestcaseResponse(testcase.getId(), testcase.getTestcase(), testcase.getAnswer(), testcase.getRole().toString(), testcase.isPublic());
    }
}
