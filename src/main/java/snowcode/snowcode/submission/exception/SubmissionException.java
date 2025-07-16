package snowcode.snowcode.submission.exception;

import lombok.Getter;

@Getter
public class SubmissionException extends RuntimeException {
    private SubmissionErrorCode code;
    private String message;

    public SubmissionException(SubmissionErrorCode code) {
        super();
        this.code = code;
        this.message = code.getMessage();
    }

    public SubmissionException(SubmissionErrorCode code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
