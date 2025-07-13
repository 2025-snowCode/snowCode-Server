package snowcode.snowcode.unit.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.common.BaseTimeEntity;
import snowcode.snowcode.course.domain.Course;
import java.time.LocalDate;

@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "unit")
public class Unit extends BaseTimeEntity {

    @Id @Column(name = "unit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "release_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Column(name = "due_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private Unit(String title, LocalDate releaseDate, LocalDate dueDate, Course course) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.dueDate = dueDate;
        this.course = course;
    }

    public static Unit createUnit(String title, LocalDate releaseDate, LocalDate dueDate, Course course) {
        return new Unit(title, releaseDate, dueDate, course);
    }

    public void updateUnit(String title, LocalDate releaseDate, LocalDate dueDate) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.dueDate = dueDate;
    }
}
