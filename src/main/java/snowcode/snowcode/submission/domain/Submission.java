package snowcode.snowcode.submission.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.auth.domain.Member;

import java.time.LocalDateTime;

@Table(name = "submission")
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission {

    @Id @Column(name = "submission_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_registration_id", nullable = false)
    private AssignmentRegistration assignmentRegistration;


    private Submission(int score, Member member, AssignmentRegistration assignmentRegistration) {
        this.score = score;
        this.member = member;
        this.assignmentRegistration = assignmentRegistration;
        this.submittedAt = LocalDateTime.now();
    }

    public static Submission createSubmission(int score, Member member, AssignmentRegistration assignmentRegistration) {
        return new Submission(score, member, assignmentRegistration);
    }
}
