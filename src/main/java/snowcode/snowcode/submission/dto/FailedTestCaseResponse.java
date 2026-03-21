package snowcode.snowcode.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FailedTestCaseResponse(
        @Schema(description = "틀린 TC id", example = "4")
        Long testcaseId,
        @Schema(description = "실제 출력", example = "43")
        String actual,
        @Schema(description = "예상 출력", example = "42")
        String expected
) {
}
