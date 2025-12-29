package snowcode.snowcode.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.unit.dto.UnitProgressResponse;
import snowcode.snowcode.unit.dto.UnitWithAssignmentScoreResponse;

import java.util.List;

public record StudentResponse(
        @Schema(description = "회원 id", example = "2")
        Long id,
        @Schema(description = "회원명", example = "주아정")
        String name,
        @Schema(description = "학번", example = "2313398")
        String studentId,
        @Schema(description = "이메일", example = "ajung7038@gmail.com")
        String email,
        @Schema(description = "과제명", example = "소프트웨어의이해")
        String title,
        @Schema(description = "받은 점수", example = "35")
        int score,
        @Schema(description = "과제 총 점수", example = "70")
        int totalScore,
        @Schema(description = "단원 수", example = "3")
        int unitCount,
        @Schema(description = "유닛 상태")
        List<UnitProgressResponse> progress,
        @Schema(description = "과제내용 + 점수")
        List<UnitWithAssignmentScoreResponse> units) {

    public static StudentResponse of (Member student, Course course, int score, int totalScore, List<UnitProgressResponse> progress, List<UnitWithAssignmentScoreResponse> units) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getStudentId(),
                student.getEmail(),
                course.getTitle(),
                score,
                totalScore,
                progress.size(),
                progress,
                units
                );
    }
}