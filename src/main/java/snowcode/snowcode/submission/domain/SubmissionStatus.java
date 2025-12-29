package snowcode.snowcode.submission.domain;

import snowcode.snowcode.submission.exception.SubmissionErrorCode;
import snowcode.snowcode.submission.exception.SubmissionException;

import java.util.Arrays;

public enum SubmissionStatus {
    CORRECT, INCORRECT, NOT_SUBMITTED;

    public static SubmissionStatus of (String status) {
        return Arrays.stream(SubmissionStatus.values())
                .filter(s -> s.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new SubmissionException(SubmissionErrorCode.INVALID_SUBMISSION_STATUS));
    }
}
