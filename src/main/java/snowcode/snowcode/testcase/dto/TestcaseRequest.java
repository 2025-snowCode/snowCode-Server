package snowcode.snowcode.testcase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TestcaseRequest(@NotBlank String testcase, @NotBlank String answer, @NotBlank String role, @NotNull boolean isPublic) {
}
