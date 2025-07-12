package snowcode.snowcode.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;

public record AddProfileResponse (Long id,
                                  @NotBlank String name,
                                  @NotBlank Role role,
                                  @NotBlank String studentId,
                                  @Email String email) {

    public static AddProfileResponse from (Member member) {
        return new AddProfileResponse(member.getId(), member.getName(), member.getRole(), member.getStudentId(), member.getEmail());
    }

}
