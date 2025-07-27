package snowcode.snowcode.assignmentRegistration.dto;

import java.util.List;

public record RegistrationScheduleResponse(int count, List<RegistrationUpcomingDateResponse> schedule) {
}
