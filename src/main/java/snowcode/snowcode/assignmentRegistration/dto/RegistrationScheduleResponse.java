package snowcode.snowcode.assignmentRegistration.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record RegistrationScheduleResponse(
        @Schema(description = "스케줄 개수", example = "2")
        int count,
        @Schema(description = "스케줄")
        List<RegistrationUpcomingDateResponse> schedule) {
}
