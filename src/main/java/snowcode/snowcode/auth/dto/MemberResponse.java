package snowcode.snowcode.auth.dto;

import jakarta.validation.constraints.NotBlank;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;

public record MemberResponse(Long id, String name, Role role, String email) {

    public static MemberResponse from (Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getRole(), member.getEmail());
    }
}
