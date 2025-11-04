package snowcode.snowcode.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.testcase.dto.TestcaseInfoResponse;

import java.util.List;

public record AssignmentInfoResponse(
        @Schema(description = "과제 id", example = "1")
        Long id,
        @Schema(description = "과제명", example = "파이썬으로 계산기 만들기")
        String title,
        @Schema(description = "과제 설명", example = "1+2=3, 4+5=9")
        String description,
        @Schema(description = "테스트케이스 개수", example = "1")
        int count,
        @Schema(description = "테스트케이스")
        List<TestcaseInfoResponse> testcases) {

    public static AssignmentInfoResponse from (Assignment assignment, List<TestcaseInfoResponse> testcases) {
        return new AssignmentInfoResponse(assignment.getId(), assignment.getTitle(), assignment.getDescription(), testcases.size(), testcases);
    }
}
