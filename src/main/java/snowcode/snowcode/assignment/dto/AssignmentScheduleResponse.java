package snowcode.snowcode.assignment.dto;

import java.util.List;

public record AssignmentScheduleResponse(int count, List<AssignmentUpcomingDateResponse> schedule) {
}
