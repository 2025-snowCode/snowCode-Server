package snowcode.snowcode.course.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.common.BaseTimeEntity;
import snowcode.snowcode.course.dto.CourseRequest;

@Entity @Getter
@Table(name = "course")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseTimeEntity {

    @Id @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String section;

    @Column(nullable = false)
    private int year;

    @Enumerated(EnumType.STRING)
    private Semester semester;

    private String description;

    public Course(String name, String section, int year, Semester semester, String description) {
        this.name = name;
        this.section = section;
        this.year = year;
        this.semester = semester;
        this.description = description;
    }

    public static Course createCourse(String name, String section, int year, Semester semester, String description) {
        return new Course(name, section, year, semester, description);
    }

    public void updateCourse(String name, String section, int year, Semester semester, String description) {
        this.name = name;
        this.section = section;
        this.year = year;
        this.semester = semester;
        this.description = description;
    }
}