package snowcode.snowcode.course.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.common.BaseTimeEntity;

@Entity @Getter
@Table(name = "course")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseTimeEntity {

    @Id @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String section;

    @Column(nullable = false)
    private int year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semester semester;

    private String description;
    private Long createdBy;

    private Course(Long memberId, String title, String section, int year, Semester semester, String description) {
        this.createdBy = memberId;
        this.title = title;
        this.section = section;
        this.year = year;
        this.semester = semester;
        this.description = description;
    }

    public static Course createCourse(Long memberId, String title, String section, int year, Semester semester, String description) {
        return new Course(memberId, title, section, year, semester, description);
    }

    public void updateCourse(String title, String section, int year, Semester semester, String description) {
        this.title = title;
        this.section = section;
        this.year = year;
        this.semester = semester;
        this.description = description;
    }
}