package snowcode.snowcode.assignment.dto;

import snowcode.snowcode.assignment.domain.Assignment;

public record AssignmentResponse(Long id, String title, int score, String description) {

    public static AssignmentResponse from (Assignment assignment) {
        return new AssignmentResponse(assignment.getId(), assignment.getTitle(), assignment.getScore(), assignment.getDescription());
    }
}
