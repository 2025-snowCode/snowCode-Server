package snowcode.snowcode.assignmentRegistration.dto;

import java.util.List;

public record RegistrationUpcomingDateResponse(String date, int remainingDays, List<RegistrationScheduleDetailResponse> assignments) {
}
