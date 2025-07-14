package snowcode.snowcode.assignment.exception;

import lombok.Getter;

@Getter
public class AssignmentException extends RuntimeException {
    private AssignmentErrorCode code;
    private String message;

    public AssignmentException(AssignmentErrorCode code) {
        super();
        this.code = code;
        this.message = code.getMessage();
    }

    public AssignmentException(AssignmentErrorCode code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
