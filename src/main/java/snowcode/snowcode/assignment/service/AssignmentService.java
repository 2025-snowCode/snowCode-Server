package snowcode.snowcode.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.*;
import snowcode.snowcode.assignment.exception.AssignmentErrorCode;
import snowcode.snowcode.assignment.exception.AssignmentException;
import snowcode.snowcode.assignment.repository.AssignmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;

    @Transactional
    public Assignment createAssignment(String title, int score, String description) {
        Assignment assignment = Assignment.createAssignment(title, score, description);
        assignmentRepository.save(assignment);
        return assignment;
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

    public List<Assignment> findAllAssignmentById(List<Long> assignmentIds) {
        return assignmentRepository.findAllById(assignmentIds);
    }

    @Transactional
    public void deleteAssignment(Long id) {
        Assignment assignment = findById(id);
        assignmentRepository.delete(assignment);
    }
}
