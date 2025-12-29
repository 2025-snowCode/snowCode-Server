package snowcode.snowcode.enrollment.exception;

import lombok.Getter;

@Getter
public class EnrollmentException extends RuntimeException {
    private EnrollmentErrorCode code;
    private String message;

    public EnrollmentException(EnrollmentErrorCode code) {
        super();
        this.code = code;
        this.message = code.getMessage();
    }

    public EnrollmentException(EnrollmentErrorCode code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
