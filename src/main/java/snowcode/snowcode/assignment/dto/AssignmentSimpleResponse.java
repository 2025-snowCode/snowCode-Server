package snowcode.snowcode.assignment.dto;

import snowcode.snowcode.assignment.domain.Assignment;

public record AssignmentSimpleResponse(Long id, String title) {

    public static AssignmentSimpleResponse from(Assignment assignment) {
        return new AssignmentSimpleResponse(assignment.getId(), assignment.getTitle());
    }
}
