package snowcode.snowcode.student.dto;

import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.unit.dto.UnitProgressResponse;

import java.util.List;

public record StudentProgressResponse(Long id, String studentId, String name, int score, int totalScore, List<UnitProgressResponse> progress) {

    public static StudentProgressResponse of (Member member, int score, int totalScore, List<UnitProgressResponse> progress) {
        return new StudentProgressResponse(member.getId(), member.getStudentId(), member.getName(), score, totalScore, progress);
    }
}
