package snowcode.snowcode.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;

public record AddProfileResponse (
        @Schema(description = "회원 id", example = "1")
        Long id,
        @Schema(description = "회원명", example = "주아정")
        String name,
        @Schema(description = "학생/관리자", example = "USER")
        Role role,
        @Schema(description = "학번", example = "2313398")
        String studentId,
        @Schema(description = "이메일", example = "ajung7038@gmail.com")
        String email) {

    public static AddProfileResponse from (Member member) {
        return new AddProfileResponse(member.getId(), member.getName(), member.getRole(), member.getStudentId(), member.getEmail());
    }

}
