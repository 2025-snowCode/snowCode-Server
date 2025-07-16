package snowcode.snowcode.submission.dto;

import snowcode.snowcode.submission.domain.Submission;

public record SubmissionResponse(int score) {

    public static SubmissionResponse of(Submission submission) {
        return new SubmissionResponse(submission.getScore());
    }
}
