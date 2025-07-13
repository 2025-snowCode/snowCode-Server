package snowcode.snowcode.course.domain;

import lombok.Getter;
import snowcode.snowcode.course.exception.CourseErrorCode;
import snowcode.snowcode.course.exception.CourseException;

import java.util.Arrays;

@Getter
public enum Semester {
    FIRST, SECOND, SUMMER, WINTER;

    public static Semester of(String semester) {
        return Arrays.stream(Semester.values())
                .filter(r -> r.name().equalsIgnoreCase(semester))
                .findFirst()
                .orElseThrow(() -> new CourseException(CourseErrorCode.INVALID_SEMESTER));
    }
}
