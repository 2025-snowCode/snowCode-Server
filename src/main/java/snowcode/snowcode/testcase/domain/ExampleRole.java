package snowcode.snowcode.testcase.domain;

import snowcode.snowcode.testcase.exception.TestcaseErrorCode;
import snowcode.snowcode.testcase.exception.TestcaseException;

import java.util.Arrays;

public enum ExampleRole {
    TESTCASE, EXAMPLE;

    public static ExampleRole of(String role) {
        return Arrays.stream(ExampleRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new TestcaseException(TestcaseErrorCode.INVALID_TESTCASE_ROLE_TYPE));
    }
}
