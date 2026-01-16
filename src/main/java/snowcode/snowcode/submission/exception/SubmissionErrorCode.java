package snowcode.snowcode.submission.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubmissionErrorCode {

    SUBMISSION_NOT_FOUND("제출 정보가 존재하지 않습니다."),
    INVALID_SUBMISSION_STATUS("올바른 제출 상태가 아닙니다."),
    FILE_NOT_FOUND("코드를 찾을 수 없습니다."),
    FILE_RUN_FAILED("파이썬 파일 실행에 실패하였습니다."),
    FILE_CREATE_FAILED("파이썬 파일을 생성할 수 없습니다."),
    FILE_DELETE_FAILED("파일 삭제에 실패하였습니다.");

    private final String message;
}
