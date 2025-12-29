package snowcode.snowcode.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AddProfileRequest (
        @Schema(description = "학번", example = "2313398")
        @NotBlank String studentId) {
}
