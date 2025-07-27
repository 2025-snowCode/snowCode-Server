package snowcode.snowcode.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import snowcode.snowcode.testcase.dto.TestcaseCreateRequest;
import snowcode.snowcode.testcase.dto.TestcaseRequest;

import java.util.List;

public record AssignmentCreateWithTestcaseRequest(@NotBlank String title, @NotNull int score, String description, List<TestcaseCreateRequest> testcases) {
}
