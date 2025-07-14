package snowcode.snowcode.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssignmentRequest(@NotBlank String title, @NotNull int score, String description) {
}
