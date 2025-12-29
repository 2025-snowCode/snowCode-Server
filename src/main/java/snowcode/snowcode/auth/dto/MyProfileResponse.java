package snowcode.snowcode.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.auth.domain.Member;

public record MyProfileResponse (
        @Schema(description = "회원명", example = "주아정")
        String name) {

    public static MyProfileResponse from (Member member) {
        return new MyProfileResponse(member.getName());
    }
}
