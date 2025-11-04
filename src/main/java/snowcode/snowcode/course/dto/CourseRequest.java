package snowcode.snowcode.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import snowcode.snowcode.student.dto.StudentRequest;

import java.util.List;

public record CourseRequest(
        @Schema(description = "강의명", example = "소프트웨어의이해")
        @NotBlank String title,
        @Schema(description = "분반", example = "001")
        @NotBlank String section,
        @Schema(description = "개설 연도", example = "2025")
        @NotNull int year,
        @Schema(description = "개설 학기", example = "SUMMER")
        @NotBlank String semester,
        @Schema(description = "강의 소개", example = "소프트웨어의 이해는 파이썬을 실습합니다.")
        String description,
        @Schema(description = "학생 리스트")
        List<StudentRequest> students) {
}
