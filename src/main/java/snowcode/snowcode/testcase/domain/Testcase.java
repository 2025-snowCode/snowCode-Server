package snowcode.snowcode.testcase.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.assignment.domain.Assignment;

@Entity @Getter
@Table(name = "testcase")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Testcase {

    @Id @Column(name = "testcase_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String testcase;

    @Column(nullable = false)
    private String answer;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    private Testcase(Assignment assignment, String testcase, String answer, boolean isPublic) {
        this.assignment = assignment;
        this.testcase = testcase;
        this.answer = answer;
        this.isPublic = isPublic;
    }

    public static Testcase createTestCase(Assignment assignment, String testcase, String answer, boolean isPublic) {
        return new Testcase(assignment, testcase, answer, isPublic);
    }
}
