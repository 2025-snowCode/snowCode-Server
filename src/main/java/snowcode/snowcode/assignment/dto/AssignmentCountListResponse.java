package snowcode.snowcode.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record AssignmentCountListResponse(
        @Schema(description = "Assignment size", example = "2")
        int count, List<AssignmentListResponse> assignments) {
}
