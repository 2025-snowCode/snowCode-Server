package snowcode.snowcode.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import snowcode.snowcode.assignment.exception.AssignmentException;
import snowcode.snowcode.auth.exception.AuthException;
import snowcode.snowcode.code.exception.CodeException;
import snowcode.snowcode.common.exception.ValidationException;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ErrorEntity;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.course.exception.CourseException;
import snowcode.snowcode.enrollment.exception.EnrollmentException;
import snowcode.snowcode.submission.exception.SubmissionException;
import snowcode.snowcode.testcase.exception.TestcaseException;
import snowcode.snowcode.unit.exception.UnitException;

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
            case MEMBER_NOT_FOUND, STUDENT_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_USER_ROLE, IS_ALREADY_ENROLLED_STUDENT -> HttpStatus.BAD_REQUEST;
            case INVALID_ROLE,
                 INVALID_ASSIGNMENT_ROLE,
                 INVALID_COURSE_ROLE,
                 INVALID_ENROLLED_ROLE,
                 INVALID_CODE_ROLE -> HttpStatus.FORBIDDEN;
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

    @ExceptionHandler(UnitException.class)
    public ResponseEntity<BasicResponse<ErrorEntity>> unitException(UnitException e) {
        HttpStatus status = switch(e.getCode()) {
            case UNIT_NOT_FOUND -> HttpStatus.NOT_FOUND;
        };
        log.error("Unit Exception({}) = {}", e.getCode(), e.getMessage());
        BasicResponse<ErrorEntity> error = ResponseUtil.error(
                new ErrorEntity(e.getCode().toString(), e.getMessage())
        );
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BasicResponse<ErrorEntity> validationDateException (ValidationException e) {
        log.error("Date Exception({}) = {}", "INVALID_DATE", e.getMessage());
        return ResponseUtil.error(new ErrorEntity("INVALID_DATE", e.getMessage())
        );
    }

    @ExceptionHandler(AssignmentException.class)
    public ResponseEntity<BasicResponse<ErrorEntity>> assignmentException(AssignmentException e) {
        HttpStatus status = switch(e.getCode()) {
            case ASSIGNMENT_NOT_FOUND, ASSIGNMENT_REGISTRATION_NOT_FOUND -> HttpStatus.NOT_FOUND;
        };
        log.error("Assignment Exception({}) = {}", e.getCode(), e.getMessage());
        BasicResponse<ErrorEntity> error = ResponseUtil.error(
                new ErrorEntity(e.getCode().toString(), e.getMessage())
        );
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(EnrollmentException.class)
    public ResponseEntity<BasicResponse<ErrorEntity>> enrollmentException(EnrollmentException e) {
        HttpStatus status = switch(e.getCode()) {
            case ENROLLMENT_NOT_FOUND -> HttpStatus.NOT_FOUND;
        };
        log.error("Enrollment Exception({}) = {}", e.getCode(), e.getMessage());
        BasicResponse<ErrorEntity> error = ResponseUtil.error(
                new ErrorEntity(e.getCode().toString(), e.getMessage())
        );
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(SubmissionException.class)
    public ResponseEntity<BasicResponse<ErrorEntity>> submissionException(SubmissionException e) {
        HttpStatus status = switch(e.getCode()) {
            case SUBMISSION_NOT_FOUND, FILE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_SUBMISSION_STATUS -> HttpStatus.BAD_REQUEST;
            case FILE_CREATE_FAILED, FILE_DELETE_FAILED -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        log.error("Submission Exception({}) = {}", e.getCode(), e.getMessage());
        BasicResponse<ErrorEntity> error = ResponseUtil.error(
                new ErrorEntity(e.getCode().toString(), e.getMessage())
        );
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(TestcaseException.class)
    public ResponseEntity<BasicResponse<ErrorEntity>> testcaseException(TestcaseException e) {
        HttpStatus status = switch(e.getCode()) {
            case TESTCASE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_TESTCASE_ROLE_TYPE -> HttpStatus.BAD_REQUEST;
        };
        log.error("Testcase Exception({}) = {}", e.getCode(), e.getMessage());
        BasicResponse<ErrorEntity> error = ResponseUtil.error(
                new ErrorEntity(e.getCode().toString(), e.getMessage())
        );
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(CodeException.class)
    public ResponseEntity<BasicResponse<ErrorEntity>> codeException(CodeException e) {
        HttpStatus status = switch(e.getCode()) {
            case CODE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_LANGUAGE_TYPE -> HttpStatus.BAD_REQUEST;
        };
        log.error("Code Exception({}) = {}", e.getCode(), e.getMessage());
        BasicResponse<ErrorEntity> error = ResponseUtil.error(
                new ErrorEntity(e.getCode().toString(), e.getMessage())
        );
        return new ResponseEntity<>(error, status);
    }
}
