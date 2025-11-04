package snowcode.snowcode.assignmentRegistration.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegistrationScheduleDetailResponse(
        @Schema(description = "할당된 과제 id", example = "1")
        Long id,
        @Schema(description = "할당된 과제가 속해 있는 강의", example = "소프트웨어의 이해")
        String course,
        @Schema(description = "할당된 과제가 속해 있는 학기", example = "SUMMER")
        String section,
        @Schema(description = "과제명", example = "음수 구별하기")
        String assignment) {
}
