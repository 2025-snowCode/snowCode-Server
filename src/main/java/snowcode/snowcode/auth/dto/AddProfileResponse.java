package snowcode.snowcode.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;

public record AddProfileResponse (Long id,
                                  String name,
                                  Role role,
                                  String studentId,
                                  String email) {

    public static AddProfileResponse from (Member member) {
        return new AddProfileResponse(member.getId(), member.getName(), member.getRole(), member.getStudentId(), member.getEmail());
    }

}
