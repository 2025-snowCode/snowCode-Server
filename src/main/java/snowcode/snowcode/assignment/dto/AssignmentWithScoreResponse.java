package snowcode.snowcode.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.assignment.domain.Assignment;

public record AssignmentWithScoreResponse(
        @Schema(description = "과제 id", example = "1")
        Long id,
        @Schema(description = "과제명", example = "파이썬으로 계산기 만들기")
        String title,
        @Schema(description = "정답 여부", example = "tre")
        boolean isCorrect,
        @Schema(description = "받은 점수", example = "30")
        int score,
        @Schema(description = "과제 총 점수", example = "100")
        int totalScore,
        @Schema(description = "제출한 코드의 id", example = "1")
        Long submittedCodeId) {

    public static AssignmentWithScoreResponse of (Assignment assignment, int score, Long submittedCodeId) {
        int totalScore = assignment.getScore();
        return new AssignmentWithScoreResponse(assignment.getId(), assignment.getTitle(), score == totalScore, score, totalScore, submittedCodeId);
    }
}