package snowcode.snowcode.course.exception;

import lombok.Getter;

@Getter
public class CourseException extends RuntimeException {
    private CourseErrorCode code;
    private String message;

    public CourseException(CourseErrorCode code) {
        super();
        this.code = code;
        this.message = code.getMessage();
    }

    public CourseException(CourseErrorCode code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
