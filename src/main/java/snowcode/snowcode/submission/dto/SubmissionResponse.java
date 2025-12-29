package snowcode.snowcode.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.submission.domain.Submission;

public record SubmissionResponse(
        @Schema(description = "받은 점수 (코드)", example = "30")
        int score) {

    public static SubmissionResponse of(Submission submission) {
        return new SubmissionResponse(submission.getScore());
    }
}
