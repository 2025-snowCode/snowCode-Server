package snowcode.snowcode.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SubmissionResponse(
        @Schema(description = "제출 코드 id", example = "1")
        Long codeId,
        @Schema(description = "정답 여부", example = "false")
        boolean isSuccess,
        @Schema(description = "만점 점수", example = "100")
        int totalScore,
        @Schema(description = "총 테스트케이스 개수", example = "10")
        int totalCount,
        @Schema(description = "맞은 테스트케이스 개수", example = "3")
        int passCount,
        @Schema(description = "실패 TC 정보")
        FailedTestCaseResponse failedTestCase
) {

    public static SubmissionResponse of(Long codeId, JudgeResultDto dto) {
        return new SubmissionResponse(codeId, dto.totalCount() == dto.passCount(), dto.totalScore(), dto.totalCount(), dto.passCount(),
                new FailedTestCaseResponse(dto.testcaseId(), dto.actual(), dto.expected()));
    }
}
