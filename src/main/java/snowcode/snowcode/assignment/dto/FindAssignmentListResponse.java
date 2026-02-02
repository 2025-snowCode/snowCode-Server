package snowcode.snowcode.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record FindAssignmentListResponse(@Schema(description = "총 과제 개수", example = "1")
                                         int count,
                                         @Schema(description = "과제 리스트")
                                         List<FindAssignmentResponse> assignments) {
}
