package snowcode.snowcode.auth.dto;

import jakarta.validation.constraints.NotBlank;
import snowcode.snowcode.auth.domain.Member;

public record MemberResponse(long id, @NotBlank String name, String role, String email) {

    public static MemberResponse from (Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getRole(), member.getEmail());
    }
}
