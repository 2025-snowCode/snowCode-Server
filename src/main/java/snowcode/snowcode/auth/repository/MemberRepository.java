package snowcode.snowcode.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByStudentId(String studentId);

    List<Member> findAllByStudentIdIn(List<String> studentIds);

    @Query("""
        select distinct m
        from Enrollment e
        join e.member m
        where e.course.id = :courseId
          and m.role <> :admin
    """)
    List<Member> findNonAdminByCourseId(@Param("courseId") Long courseId,
                                        @Param("admin") Role admin);

    @Query("""
        select m
        from Enrollment e
        join e.member m
        where e.course.id = :courseId
          and m.role <> :admin
          and m.studentId = :studentId
    """)
    Optional<Member> findNonAdminByCourseIdAndStudentId(@Param("courseId") Long courseId,
                                                        @Param("studentId") String studentId,
                                                        @Param("admin") Role admin);
}
