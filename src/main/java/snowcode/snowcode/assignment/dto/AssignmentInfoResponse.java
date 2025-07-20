package snowcode.snowcode.assignment.dto;

import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.testcase.dto.TestcaseInfoResponse;

import java.util.List;

public record AssignmentInfoResponse(Long id, String title, String description, int count, List<TestcaseInfoResponse> testcases) {

    public static AssignmentInfoResponse of (Assignment assignment, List<TestcaseInfoResponse> testcases) {
        return new AssignmentInfoResponse(assignment.getId(), assignment.getTitle(), assignment.getDescription(), testcases.size(), testcases);
    }
}
