package snowcode.snowcode.unit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.AssignmentDetailAdminResponse;
import snowcode.snowcode.assignment.dto.AssignmentDetailStudentResponse;
import snowcode.snowcode.assignment.service.AssignmentService;
import snowcode.snowcode.submission.service.SubmissionWithAssignmentFacade;
import snowcode.snowcode.unit.domain.Unit;
import snowcode.snowcode.unit.dto.UnitDetailAdminResponse;
import snowcode.snowcode.unit.dto.UnitDetailStudentResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitWithAssignmentFacade {
    private final SubmissionWithAssignmentFacade submissionWithAssignmentFacade;
    private final UnitService unitService;
    private final AssignmentService assignmentService;

    public UnitDetailStudentResponse createStudentUnitResponse(Long memberId, Long unitId) {
        Unit unit = unitService.findUnit(unitId);
        List<Assignment> assignmentList = assignmentService.findAllByUnitId(unitId);

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
        List<Assignment> assignmentList = assignmentService.findAllByUnitId(unitId);

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
}
