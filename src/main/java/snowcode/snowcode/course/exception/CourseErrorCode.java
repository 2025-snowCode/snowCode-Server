package snowcode.snowcode.course.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourseErrorCode {

    COURSE_NOT_FOUND("강의가 존재하지 않습니다."),
    INVALID_SEMESTER("학기 입력이 잘못되었습니다."),
    INVALID_YEAR("연도 입력이 잘못되었습니다.");

    private final String message;
}
