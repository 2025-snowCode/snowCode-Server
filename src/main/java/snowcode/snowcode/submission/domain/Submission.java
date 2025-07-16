package snowcode.snowcode.submission.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.auth.domain.Member;

@Table(name = "submission")
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission {

    @Id @Column(name = "submission_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;


    private Submission(int score, Member member, Assignment assignment) {
        this.score = score;
        this.member = member;
        this.assignment = assignment;
    }

    public static Submission createSubmission(int score, Member member, Assignment assignment) {
        return new Submission(score, member, assignment);
    }
}
