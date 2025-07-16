package snowcode.snowcode.testcase.exception;

import lombok.Getter;

@Getter
public class TestcaseException extends RuntimeException {
    private TestcaseErrorCode code;
    private String message;

    public TestcaseException(TestcaseErrorCode code) {
        super();
        this.code = code;
        this.message = code.getMessage();
    }

    public TestcaseException(TestcaseErrorCode code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
