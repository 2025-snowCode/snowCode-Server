package snowcode.snowcode.testcase.dto;

import jakarta.validation.constraints.NotBlank;

public record TestcaseRequest(@NotBlank String testcase, @NotBlank String answer, @NotBlank String role, @NotBlank boolean isPublic) {
}
