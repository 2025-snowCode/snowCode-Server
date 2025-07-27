package snowcode.snowcode.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.dto.CourseDetailAdminResponse;
import snowcode.snowcode.course.dto.CourseDetailStudentResponse;
import snowcode.snowcode.unit.domain.Unit;
import snowcode.snowcode.unit.dto.UnitDetailAdminResponse;
import snowcode.snowcode.unit.dto.UnitDetailStudentResponse;
import snowcode.snowcode.unit.service.UnitService;
import snowcode.snowcode.unit.service.UnitWithAssignmentFacade;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseWithMemberFacade {
    private final CourseService courseService;
    private final UnitService unitService;
    private final UnitWithAssignmentFacade unitWithAssignmentFacade;
    private final CourseWithEnrollmentFacade courseWithEnrollmentFacade;

    public CourseDetailStudentResponse createStudentCourseResponse(Long memberId, Long courseId) {
        Course course = courseService.findCourse(courseId);
        List<Unit> unitList = unitService.findAllByCourseId(courseId);

        List<UnitDetailStudentResponse> unitDtoList = new ArrayList<>();

        for (Unit unit : unitList) {
            unitDtoList.add(unitWithAssignmentFacade.createStudentUnitResponse(memberId, unit.getId()));
        }

        return new CourseDetailStudentResponse(
                courseId,
                course.getTitle(),
                course.getYear(),
                course.getSemester().toString(),
                course.getSection(),
                unitDtoList.size(),
                unitDtoList
        );
    }

    public CourseDetailAdminResponse createAdminCourseResponse(Long courseId) {
        Course course = courseService.findCourse(courseId);
        List<Unit> unitList = unitService.findAllByCourseId(courseId);

        List<UnitDetailAdminResponse> unitDtoList = new ArrayList<>();

        for (Unit unit : unitList) {
            unitDtoList.add(unitWithAssignmentFacade.createAdminUnitResponse(unit.getId()));
        }

        int size = courseWithEnrollmentFacade.findNonAdminByCourseId(courseId).size();

        return new CourseDetailAdminResponse(
                courseId,
                course.getTitle(),
                course.getYear(),
                course.getSemester().toString(),
                course.getSection(),
                size,
                unitDtoList.size(),
                unitDtoList
        );
    }
}
