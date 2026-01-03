package snowcode.snowcode.common;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import snowcode.snowcode.assignment.dto.AssignmentCreateWithTestcaseRequest;
import snowcode.snowcode.assignment.service.AssignmentWithTestcaseFacade;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.dto.login.LoginRequest;
import snowcode.snowcode.auth.service.AuthService;
import snowcode.snowcode.auth.service.MemberService;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.dto.CourseRequest;
import snowcode.snowcode.course.service.CourseService;
import snowcode.snowcode.course.service.CourseWithEnrollmentFacade;
import snowcode.snowcode.student.dto.StudentRequest;
import snowcode.snowcode.unit.dto.UnitRequest;
import snowcode.snowcode.unit.service.UnitWithAssignmentFacade;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("local")
@RequiredArgsConstructor
public class TestDataInitializer {

    private final AuthService authService;
    private final CourseWithEnrollmentFacade courseWithEnrollmentFacade;
    private final MemberService memberService;
    private final AssignmentWithTestcaseFacade assignmentWithTestcaseFacade;
    private final UnitWithAssignmentFacade unitWithAssignmentFacade;
    private final CourseService courseService;

    private final int ADMIN_LOOP = 75;
    private final int USER_LOOP = 3750;
    private final int COURSE_LOOP = 75;
    private final int ASSIGNMENT_LOOP = 10;
    private final int UNIT_LOOP = 5;
    private final int STUDENT_LOOP = 50;


    @PostConstruct
    @Transactional
    public void init() throws GeneralSecurityException {
        System.out.println("test");
        createMember();
        createCourse();
        createAssignment();
        createUnit();
    }

    private void createMember() throws GeneralSecurityException {

        // user
        for (int i=0; i<USER_LOOP; i++) {
            LoginRequest dto = new LoginRequest("LOCAL", "test"+i, "USER", String.valueOf(i), "test"+i+"@gmail.com", "sM0yOK1FPuGJaq8x/U76gkKNfT64GQKsityED54zG9M=");
            authService.login(dto);
        }

        // admin
        for (int i=USER_LOOP; i<USER_LOOP + ADMIN_LOOP; i++) {
            LoginRequest dto = new LoginRequest("LOCAL", "test"+i, "ADMIN", null,"test"+i+"@gmail.com", "sM0yOK1FPuGJaq8x/U76gkKNfT64GQKsityED54zG9M=");
            authService.login(dto);
        }


    }

    private void createCourse() {
        for (int i=0; i<COURSE_LOOP; i++) {
            Member member = memberService.findMember((long) USER_LOOP + (i + 1));

            // 50명씩 할당
            List<StudentRequest> students = new ArrayList<>();
            int start = i * STUDENT_LOOP;
            int end = start + STUDENT_LOOP;

            for (int j = start; j < end; j++) {
                StudentRequest studentRequest = new StudentRequest(String.valueOf(j));
                students.add(studentRequest);
            }

            CourseRequest courseRequest = new CourseRequest("강의명"+i, "001", 2026, "SUMMER", "강의 소개", students);
            courseWithEnrollmentFacade.createCourseWithEnroll(member, courseRequest);
        }
    }

    private void createAssignment() {

        for (int i=USER_LOOP; i<USER_LOOP + ADMIN_LOOP; i++) {
            Member member = memberService.findMember((long)(i + 1));

            int start = i * ASSIGNMENT_LOOP;
            int end = start + ASSIGNMENT_LOOP;

            for (int j = start; j < end; j++) {
                AssignmentCreateWithTestcaseRequest dto = new AssignmentCreateWithTestcaseRequest("과제"+(j-37499), 100, "설명", null);
                assignmentWithTestcaseFacade.createAssignment(member.getId(), dto);

            }
        }
    }

    private void createUnit() {
        // 강의 다섯 개 -> 총 75개
        // 강의 하나당 유닛 5개 할당
        // 유닛 하나당 과제 2개 할당
        for (int i = 0; i < COURSE_LOOP; i++) {
            Course course = courseService.findCourse((long) i + 1);

            int unitStart = i * UNIT_LOOP;
            int unitEnd = unitStart + UNIT_LOOP;

            for (int j = unitStart; j < unitEnd; j++) {

                List<Long> assignmentIds = new ArrayList<>();

                int assignmentStart = j * 2+1;
                int assignmentEnd = assignmentStart + 2;

                for (int k = assignmentStart; k < assignmentEnd; k++) {
                    assignmentIds.add((long) k);
                }

                UnitRequest dto =
                        new UnitRequest("단원명" + j, "2026-01-03", "2026-01-04", assignmentIds);

                unitWithAssignmentFacade.registrationAssignment(course, dto);
            }
        }

    }
}
