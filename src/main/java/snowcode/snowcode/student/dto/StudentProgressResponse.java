package snowcode.snowcode.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.unit.dto.UnitProgressResponse;

import java.util.List;

public record StudentProgressResponse(
        @Schema(description = "회원(학생) id", example = "2")
        Long id,
        @Schema(description = "학번", example = "2313398")
        String studentId,
        @Schema(description = "회원명", example = "주아정")
        String name,
        @Schema(description = "과제 점수", example = "30")
        int score,
        @Schema(description = "과제 총 점수", example = "100")
        int totalScore,
        @Schema(description = "과제 상태 (제출 여부, 오답 여부 등)")
        List<UnitProgressResponse> progress) {

    public static StudentProgressResponse of (Member member, int score, int totalScore, List<UnitProgressResponse> progress) {
        return new StudentProgressResponse(member.getId(), member.getStudentId(), member.getName(), score, totalScore, progress);
    }
}
