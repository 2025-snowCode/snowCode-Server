package snowcode.snowcode.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MemberCountListResponse (
        @Schema(description = "회원 수", example = "10")
        int count,
        @Schema(description = "회원")
        List<MemberResponse> members) {
}
