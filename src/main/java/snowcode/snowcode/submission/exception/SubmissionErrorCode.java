package snowcode.snowcode.submission.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubmissionErrorCode {

    SUBMISSION_NOT_FOUND("제출 정보가 존재하지 않습니다.");

    private final String message;
}
