package snowcode.snowcode.code.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.submission.domain.Submission;

@Entity @Getter
@Table(name = "code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Code {

    @Id @Column(name = "code_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Column(name = "is_submitted")
    private boolean isSubmitted;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    private Code(String code, boolean isSubmitted, Language language, Submission submission) {
        this.code = code;
        this.isSubmitted = isSubmitted;
        this.language = language;
        this.submission = submission;
    }

    public static Code createCode(String code, boolean isSubmitted, Language language, Submission submission) {
        return new Code(code, isSubmitted, language, submission);
    }
}
