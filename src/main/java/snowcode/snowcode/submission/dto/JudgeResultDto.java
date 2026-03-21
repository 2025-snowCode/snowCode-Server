package snowcode.snowcode.submission.dto;

public record JudgeResultDto(
        int totalScore,
        int totalCount,
        int passCount,
        Long testcaseId,
        String actual,
        String expected
) {
}
