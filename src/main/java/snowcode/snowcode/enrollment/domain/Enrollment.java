package snowcode.snowcode.enrollment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.common.BaseTimeEntity;
import snowcode.snowcode.course.domain.Course;

@Getter @Table(name = "enrollment")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enrollment extends BaseTimeEntity {

    @Id @Column(name = "enrollment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "total_score")
    private int totalScore;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private Enrollment(int totalScore, int score, EnrollmentStatus status, Member member, Course course) {
        this.totalScore = totalScore;
        this.score = score;
        this.status = status;
        this.member = member;
        this.course = course;
    }

    public static Enrollment createEnrollment(int totalScore, int score, EnrollmentStatus status, Member member, Course course) {
        return new Enrollment(totalScore, score, status, member, course);
    }
}
