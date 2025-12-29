package snowcode.snowcode.assignmentRegistration.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record RegistrationUpcomingDateResponse(
        @Schema(description = "마감 날짜", example = "2025-11-03")
        String date,
        @Schema(description = "남은 날짜", example = "5")
        int remainingDays,
        @Schema(description = "남은 과제 리스트")
        List<RegistrationScheduleDetailResponse> assignments) {
}
