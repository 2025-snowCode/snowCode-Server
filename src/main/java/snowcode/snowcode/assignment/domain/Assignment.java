package snowcode.snowcode.assignment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.common.BaseTimeEntity;
import snowcode.snowcode.unit.domain.Unit;

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

    private Assignment(String title, int score, String description) {
        this.title = title;
        this.score = score;
        this.description = description;
    }

    public static Assignment createAssignment(String title, int score, String description) {
        return new Assignment(title, score, description);
    }

    public void updateAssignment(String title, int score, String description) {
        this.title = title;
        this.score = score;
        this.description = description;
    }
}
