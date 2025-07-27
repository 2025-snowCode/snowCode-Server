package snowcode.snowcode.unit.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UnitRequest(@NotBlank String title,
                          @NotBlank String releaseDate,
                          @NotBlank String dueDate,
                          List<Long> assignmentIds) {
}
