package snowcode.snowcode.unit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.AssignmentDetailAdminResponse;
import snowcode.snowcode.assignment.dto.AssignmentDetailStudentResponse;
import snowcode.snowcode.assignment.dto.AssignmentSimpleResponse;
import snowcode.snowcode.assignment.service.AssignmentService;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.submission.service.SubmissionWithAssignmentFacade;
import snowcode.snowcode.unit.domain.Unit;
import snowcode.snowcode.unit.dto.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitWithAssignmentFacade {
    private final SubmissionWithAssignmentFacade submissionWithAssignmentFacade;
    private final UnitService unitService;
    private final RegistrationService registrationService;
    private final AssignmentService assignmentService;

    @Transactional
    public UnitWithAssignmentResponse registrationAssignment(Course course, UnitRequest dto) {
        Unit unit = unitService.createUnit(course, dto);
        List<Assignment> assignments = assignmentService.findAllAssignmentById(dto.assignmentIds());
        registrationService.createRegistrations(unit, assignments);

        List<AssignmentSimpleResponse> assignmentList = new ArrayList<>();
        for (Assignment assignment : assignments) {
            assignmentList.add(AssignmentSimpleResponse.from(assignment));
        }
        return UnitWithAssignmentResponse.from(unit, assignmentList);
    }

    public UnitDetailStudentResponse createStudentUnitResponse(Long memberId, Long unitId) {
        Unit unit = unitService.findUnit(unitId);
        List<AssignmentRegistration> assignmentRegistrationList = registrationService.findAllByUnitId(unitId);

        List<AssignmentDetailStudentResponse> assignmentDtoList = new ArrayList<>();

        for (AssignmentRegistration assignmentRegistration : assignmentRegistrationList) {
            assignmentDtoList.add(submissionWithAssignmentFacade.createStudentAssignmentResponse(memberId, assignmentRegistration));
        }
        boolean isOpen = unitService.isOpenUnit(unit.getReleaseDate());

        return new UnitDetailStudentResponse(
                unitId,
                unit.getTitle(),
                unit.getReleaseDate().toString(),
                unit.getDueDate().toString(),
                isOpen,
                assignmentDtoList.size(),
                assignmentDtoList
        );
    }

    public UnitDetailAdminResponse createAdminUnitResponse(Long unitId) {
        Unit unit = unitService.findUnit(unitId);
        List<AssignmentRegistration> assignmentRegistrationList = registrationService.findAllByUnitId(unitId);

        List<AssignmentDetailAdminResponse> assignmentDtoList = new ArrayList<>();

        for (AssignmentRegistration assignmentRegistration : assignmentRegistrationList) {
            assignmentDtoList.add(submissionWithAssignmentFacade.createAdminAssignmentResponse(assignmentRegistration.getId()));
        }
        boolean isOpen = unitService.isOpenUnit(unit.getReleaseDate());

        return new UnitDetailAdminResponse(
                unitId,
                unit.getTitle(),
                unit.getReleaseDate().toString(),
                unit.getDueDate().toString(),
                isOpen,
                assignmentDtoList.size(),
                assignmentDtoList
        );
    }

    public UnitWithAssignmentResponse findAllByUnitId(Long unitId) {
        Unit unit = unitService.findUnit(unitId);
        List<Assignment> assignments = registrationService.findAllByUnitId(unitId).stream().map(AssignmentRegistration::getAssignment).toList();
        List<AssignmentSimpleResponse> assignmentList = assignmentListToSimpleDto(assignments);
        return UnitWithAssignmentResponse.from(unit, assignmentList);
    }

    @Transactional
    public void deleteAllByUnitId(Long unitId) {
        registrationService.deleteAllByUnitId(unitId);
        unitService.deleteUnit(unitId);
    }

    public UnitCountListResponse findAllUnit(Long courseId) {
        List<Object[]> countUnits = unitService.countAssignmentsByCourseId(courseId);

        List<UnitListResponse> dtoList = countUnits.stream()
                .map (row -> new UnitListResponse(
                        (Long) row[0],                  // id
                        (String) row[1],                // title
                        ((Long) row[2]).intValue()      // assignmentCount
                ))
                .toList();

        return new UnitCountListResponse(dtoList.size(), dtoList);
    }

    private List<AssignmentSimpleResponse> assignmentListToSimpleDto(List<Assignment> assignments) {
        return assignments.stream()
                .map(AssignmentSimpleResponse::from)
                .toList();
    }
}
