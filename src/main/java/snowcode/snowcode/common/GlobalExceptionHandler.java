package snowcode.snowcode.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import snowcode.snowcode.auth.exception.AuthException;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ErrorEntity;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.exception.CourseException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BasicResponse<ErrorEntity> validationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        log.error("Dto Validation Exception({}) : {}", "BAD_INPUT", errors);
        return ResponseUtil.error(new ErrorEntity("BAD_INPUT", "입력이 올바르지 않습니다.", errors));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<BasicResponse<ErrorEntity>> authException(AuthException e) {
        HttpStatus status = switch(e.getCode()) {
            case MEMBER_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_USER_ROLE -> HttpStatus.BAD_REQUEST;
        };
        log.error("Auth Exception({}) = {}", e.getCode(), e.getMessage());
        BasicResponse<ErrorEntity> error = ResponseUtil.error(
                new ErrorEntity(e.getCode().toString(), e.getMessage())
        );
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(CourseException.class)
    public ResponseEntity<BasicResponse<ErrorEntity>> courseException(CourseException e) {
        HttpStatus status = switch(e.getCode()) {
            case COURSE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_SEMESTER, INVALID_YEAR -> HttpStatus.BAD_REQUEST;
        };
        log.error("Course Exception({}) = {}", e.getCode(), e.getMessage());
        BasicResponse<ErrorEntity> error = ResponseUtil.error(
                new ErrorEntity(e.getCode().toString(), e.getMessage())
        );
        return new ResponseEntity<>(error, status);
    }
}
