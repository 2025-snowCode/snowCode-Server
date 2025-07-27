package snowcode.snowcode.testcase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TestcaseCreateRequest(@NotBlank String testcase, @NotBlank String answer) {
}
