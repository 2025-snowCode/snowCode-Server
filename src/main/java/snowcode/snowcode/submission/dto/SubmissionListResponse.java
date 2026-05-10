package snowcode.snowcode.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record SubmissionListResponse(
        @Schema(description = "과제 id", example = "1")
        Long assignmentId,
        @Schema(description = "제출 내역")
        List<SubmissionWithCode> submissionList
) {
}
