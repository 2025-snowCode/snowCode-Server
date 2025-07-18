package snowcode.snowcode.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.AssignmentCountListResponse;
import snowcode.snowcode.assignment.dto.AssignmentListResponse;
import snowcode.snowcode.assignment.dto.AssignmentRequest;
import snowcode.snowcode.assignment.dto.AssignmentResponse;
import snowcode.snowcode.assignment.exception.AssignmentErrorCode;
import snowcode.snowcode.assignment.exception.AssignmentException;
import snowcode.snowcode.assignment.repository.AssignmentRepository;
import snowcode.snowcode.unit.domain.Unit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;

    @Transactional
    public AssignmentResponse createAssignment(Unit unit, AssignmentRequest dto) {
        Assignment assignment = Assignment.createAssignment(unit, dto.title(), dto.score(), dto.description());
        assignmentRepository.save(assignment);
        return AssignmentResponse.from(assignment);
    }

    public AssignmentResponse findAssignment (Long id) {
        Assignment assignment = findById(id);
        return AssignmentResponse.from(assignment);
    }

    public Assignment findById(Long id) {
        return assignmentRepository.findById(id).orElseThrow(
                () -> new AssignmentException(AssignmentErrorCode.ASSIGNMENT_NOT_FOUND)
        );
    }

    public AssignmentCountListResponse findAllAssignment() {
        List<AssignmentListResponse> lst = assignmentRepository.findAll().stream()
                .map(AssignmentListResponse::from)
                .toList();
        return new AssignmentCountListResponse(lst.size(), lst);
    }

    @Transactional
    public AssignmentResponse updateAssignment(Long id, AssignmentRequest dto) {
        Assignment assignment = findById(id);
        assignment.updateAssignment(dto.title(), dto.score(), dto.description());
        return AssignmentResponse.from(assignment);
    }

    @Transactional
    public void deleteAssignment(Long id) {
        Assignment assignment = findById(id);
        assignmentRepository.delete(assignment);
    }

    public Map<Long, Integer> countAssignmentsByCourseId(List<Long> courseIds) {
        List<Object[]> results = assignmentRepository.countAssignmentsByCourseIds(courseIds);

        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));
    }
}
