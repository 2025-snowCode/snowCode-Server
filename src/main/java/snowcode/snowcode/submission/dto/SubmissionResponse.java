package snowcode.snowcode.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.submission.domain.Submission;

public record SubmissionResponse(
        @Schema(description = "제출 코드 id", example = "1")
        long condeId,
        @Schema(description = "받은 점수 (코드)", example = "30")
        int score) {

    public static SubmissionResponse of(long codeId, Submission submission) {
        return new SubmissionResponse(codeId, submission.getScore());
    }
}
