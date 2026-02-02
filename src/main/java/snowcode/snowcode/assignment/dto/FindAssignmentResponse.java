package snowcode.snowcode.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.assignment.domain.Assignment;

import java.util.List;

public record FindAssignmentResponse(@Schema(description = "과제 id", example = "1")
                                         Long assignmentId,
                                     @Schema(description = "과제 제목", example = "파이썬으로 계산기 만들기")
                                         String title) {

    public static FindAssignmentResponse from(Assignment assignment) {
        return new FindAssignmentResponse(assignment.getId(), assignment.getTitle());
    }
}
