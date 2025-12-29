package snowcode.snowcode.enrollment.domain;

import snowcode.snowcode.enrollment.exception.EnrollmentErrorCode;
import snowcode.snowcode.enrollment.exception.EnrollmentException;

import java.util.Arrays;

public enum EnrollmentStatus {
    ENROLLED, COMPLETED;

    public static EnrollmentStatus of(String status) {
        return Arrays.stream(EnrollmentStatus.values())
                .filter(r -> r.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new EnrollmentException(EnrollmentErrorCode.ENROLLMENT_NOT_FOUND));
    }
}