package snowcode.snowcode.student.dto;

import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.unit.dto.UnitProgressResponse;
import snowcode.snowcode.unit.dto.UnitWithAssignmentScoreResponse;

import java.util.List;

public record StudentResponse(Long id, String name, String studentId, String email, String title, int score, int totalScore, int unitCount, List<UnitProgressResponse> progress, List<UnitWithAssignmentScoreResponse> units) {

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