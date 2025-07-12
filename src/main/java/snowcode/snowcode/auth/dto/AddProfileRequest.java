package snowcode.snowcode.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AddProfileRequest (@NotBlank String studentId) {
}
