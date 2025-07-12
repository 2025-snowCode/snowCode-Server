package snowcode.snowcode.auth.dto;

import java.util.List;

public record MemberCountListResponse (int count, List<MemberResponse> members) {
}
