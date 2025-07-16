package snowcode.snowcode.testcase.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExampleRole role;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    private Testcase(String testcase, String answer, ExampleRole role, boolean isPublic) {
        this.testcase = testcase;
        this.answer = answer;
        this.role = role;
        this.isPublic = isPublic;
    }

    public static Testcase createTestCase(String testcase, String answer, ExampleRole role, boolean isPublic) {
        return new Testcase(testcase, answer, role, isPublic);
    }
}
