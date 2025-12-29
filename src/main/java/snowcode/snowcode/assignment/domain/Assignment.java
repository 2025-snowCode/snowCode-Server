package snowcode.snowcode.assignment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.assignment.dto.AssignmentCreateWithTestcaseRequest;
import snowcode.snowcode.common.BaseTimeEntity;

@Entity @Getter
@Table(name = "assignment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment extends BaseTimeEntity {

    @Id @Column(name = "assignment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "total_score", nullable = false)
    private int score;

    private String description;

    @Column(nullable = false)
    private Long createdBy;

    private Assignment(Long memberId, String title, int score, String description) {
        this.createdBy = memberId;
        this.title = title;
        this.score = score;
        this.description = description;
    }

    public static Assignment createAssignment(Long memberId, AssignmentCreateWithTestcaseRequest dto) {
        return new Assignment(memberId, dto.title(), dto.score(), dto.description());
    }

    public void updateAssignment(String title, int score, String description) {
        this.title = title;
        this.score = score;
        this.description = description;
    }
}
