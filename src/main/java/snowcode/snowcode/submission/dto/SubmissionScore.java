package snowcode.snowcode.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SubmissionScore (
        @Schema(description = "회원 id", example = "1")
        Long memberId,
        @Schema(description = "코드 제출 id", example = "1")
        Long registrationId,
        @Schema(description = "총 점수", example = "10")
        int maxScore) {
}
