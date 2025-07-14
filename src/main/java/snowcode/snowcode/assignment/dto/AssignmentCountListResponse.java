package snowcode.snowcode.assignment.dto;

import java.util.List;

public record AssignmentCountListResponse(int count, List<AssignmentListResponse> assignments) {
}
