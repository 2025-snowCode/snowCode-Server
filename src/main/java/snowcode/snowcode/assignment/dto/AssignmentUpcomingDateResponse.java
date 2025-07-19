package snowcode.snowcode.assignment.dto;

import java.util.List;

public record AssignmentUpcomingDateResponse(String date, int remainingDays, List<AssignmentScheduleDetailResponse> assignments) {
}
