package snowcode.snowcode.common.exception;

import snowcode.snowcode.course.exception.CourseErrorCode;
import snowcode.snowcode.course.exception.CourseException;

public class Validator {
    public static int validYear(int year) {
        if (year <= 2000 || year >= 2100) {
            throw new CourseException(CourseErrorCode.INVALID_YEAR);
        }
        return year;
    }
}
