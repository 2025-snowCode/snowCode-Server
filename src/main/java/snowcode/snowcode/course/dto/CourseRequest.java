package snowcode.snowcode.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import snowcode.snowcode.student.dto.StudentRequest;

import java.util.List;

public record CourseRequest(@NotBlank String title,
                            @NotBlank String section,
                            @NotNull int year,
                            @NotBlank String semester,
                            String description,
                            List<StudentRequest> students) {
}
