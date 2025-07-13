package snowcode.snowcode.unit.dto;

import jakarta.validation.constraints.NotBlank;

public record UnitRequest(@NotBlank String title,
                          @NotBlank String releaseDate,
                          @NotBlank String dueDate) {
}
