package snowcode.snowcode.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.dto.AssignmentSimpleResponse;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.dto.CourseCountWithAssignmentResponse;
import snowcode.snowcode.course.dto.CourseWithAssignmentResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseWithRegistrationFacade {

    private final RegistrationService registrationService;
    private final CourseService courseService;

    public CourseCountWithAssignmentResponse findCourseTitleWithAssignments(Long memberId, Long courseId) {
        List<Long> courseIdsByTitle = courseService.findCourseIdsWithTitle(memberId, courseId);
        Map<Course, List<AssignmentSimpleResponse>> courseIdWithAssignmentMap = registrationService.findAssignmentsByCourseId(courseIdsByTitle);

        List<CourseWithAssignmentResponse> courseAssignmentDto = new ArrayList<>();
        courseIdWithAssignmentMap.forEach((course, assignments) -> {
            courseAssignmentDto.add(CourseWithAssignmentResponse.create(course, assignments));
        });
        return new CourseCountWithAssignmentResponse(courseAssignmentDto.size(), courseAssignmentDto);
    }
}
