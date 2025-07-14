package snowcode.snowcode.assignment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssignmentErrorCode {

    ASSIGNMENT_NOT_FOUND("과제가 존재하지 않습니다.");

    private final String message;
}
