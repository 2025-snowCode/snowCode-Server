package snowcode.snowcode.testcase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.testcase.domain.Testcase;

public record TestcaseInfoResponse(
        @Schema(description = "테스트케이스 id", example = "1")
        Long id,
        @Schema(description = "테스트케이스 문제", example = "1 2")
        String testcase,
        @Schema(description = "테스트케이스 정답", example = "3")
        String answer) {

    public static TestcaseInfoResponse of(Testcase testcase) {
        return new TestcaseInfoResponse(testcase.getId(), testcase.getTestcase(), testcase.getAnswer());
    }
}
