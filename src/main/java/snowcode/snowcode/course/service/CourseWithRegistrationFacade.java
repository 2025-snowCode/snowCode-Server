package snowcode.snowcode.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.dto.AssignmentSimpleResponse;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.dto.CourseCountWithAssignmentResponse;
import snowcode.snowcode.unit.domain.Unit;
import snowcode.snowcode.unit.service.UnitService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseWithRegistrationFacade {

    private final RegistrationService registrationService;
    private final CourseService courseService;
    private final UnitService unitService;

    public CourseCountWithAssignmentResponse findCourseTitleWithAssignments(Long memberId, Long courseId) {
        // 0. courseId로 course 찾기
        Course course = courseService.findCourse(courseId);
        // 1. 유닛Repo에서 강의 id로 유닛들 찾기.
        // 2. 찾은 유닛 id만 뽑아내서 List<Long> 형태로 변환
        List<Long> units = unitService.findAllByCourseId(courseId).stream().map(Unit::getId).toList();

        // 3. AssignmentRegistrationRepo에서 UnitIdList로 List<AssignmentRegistration> 찾기 -> 한 번에 찾아지는 걸로 앎
        List<AssignmentSimpleResponse> assignmentSimpleResponseList = registrationService.findAllByUnitIdIn(units)
                .stream().map(e -> AssignmentSimpleResponse.from(e.getAssignment())).toList();
        return CourseCountWithAssignmentResponse.of(course, assignmentSimpleResponseList);
    }
}
