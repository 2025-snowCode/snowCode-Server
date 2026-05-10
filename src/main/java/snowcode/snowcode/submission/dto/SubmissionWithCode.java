package snowcode.snowcode.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.submission.domain.Submission;

import java.time.LocalDate;

public record SubmissionWithCode(
        @Schema(description = "제출 코드 id", example = "1")
        Long codeId,
        @Schema(description = "정답 여부", example = "false")
        boolean isSuccess,
        @Schema(description = "제출 날짜", example = "2026-05-10")
        LocalDate submittedAt
) {

        public static SubmissionWithCode of(Long codeId, int originalScore, Submission submission) {
                return new SubmissionWithCode(codeId, originalScore == submission.getScore(), submission.getSubmittedAt().toLocalDate());
        }
}
