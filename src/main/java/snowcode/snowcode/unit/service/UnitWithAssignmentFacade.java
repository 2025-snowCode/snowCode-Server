package snowcode.snowcode.unit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.AssignmentDetailAdminResponse;
import snowcode.snowcode.assignment.dto.AssignmentDetailStudentResponse;
import snowcode.snowcode.assignmentRegistration.service.RegistrationService;
import snowcode.snowcode.submission.service.SubmissionWithAssignmentFacade;
import snowcode.snowcode.unit.domain.Unit;
import snowcode.snowcode.unit.dto.UnitCountListResponse;
import snowcode.snowcode.unit.dto.UnitDetailAdminResponse;
import snowcode.snowcode.unit.dto.UnitDetailStudentResponse;
import snowcode.snowcode.unit.dto.UnitListResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitWithAssignmentFacade {
    private final SubmissionWithAssignmentFacade submissionWithAssignmentFacade;
    private final UnitService unitService;
    private final RegistrationService registrationService;

    public UnitDetailStudentResponse createStudentUnitResponse(Long memberId, Long unitId) {
        Unit unit = unitService.findUnit(unitId);
        List<Assignment> assignmentList = registrationService.findAllByUnitId(unitId);

        List<AssignmentDetailStudentResponse> assignmentDtoList = new ArrayList<>();

        for (Assignment assignment : assignmentList) {
            assignmentDtoList.add(submissionWithAssignmentFacade.createStudentAssignmentResponse(memberId, assignment.getId()));
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
        List<Assignment> assignmentList = registrationService.findAllByUnitId(unitId);

        List<AssignmentDetailAdminResponse> assignmentDtoList = new ArrayList<>();

        for (Assignment assignment : assignmentList) {
            assignmentDtoList.add(submissionWithAssignmentFacade.createAdminAssignmentResponse(assignment.getId()));
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
}
