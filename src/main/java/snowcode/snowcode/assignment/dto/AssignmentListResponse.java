package snowcode.snowcode.assignment.dto;

import snowcode.snowcode.assignment.domain.Assignment;

public record AssignmentListResponse(Long id, String title, int score) {

    public static AssignmentListResponse from (Assignment assignment) {
        return new AssignmentListResponse(assignment.getId(), assignment.getTitle(), assignment.getScore());
    }
}
