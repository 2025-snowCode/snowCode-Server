package snowcode.snowcode.assignment.dto;

import snowcode.snowcode.assignment.domain.Assignment;

public record AssignmentWithScoreResponse(Long id, String title, boolean isCorrect, int score, int totalScore, Long submittedCodeId) {

    public static AssignmentWithScoreResponse of (Assignment assignment, int score, Long submittedCodeId) {
        int totalScore = assignment.getScore();
        return new AssignmentWithScoreResponse(assignment.getId(), assignment.getTitle(), score == totalScore, score, totalScore, submittedCodeId);
    }
}