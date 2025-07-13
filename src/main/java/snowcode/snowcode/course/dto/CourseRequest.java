package snowcode.snowcode.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseRequest(@NotBlank String name,
                            @NotBlank String section,
                            @NotNull int year,
                            @NotBlank String semester,
                            String description) {
}
