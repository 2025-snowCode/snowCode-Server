package snowcode.snowcode.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.AssignmentRequest;
import snowcode.snowcode.assignment.dto.AssignmentResponse;
import snowcode.snowcode.assignment.repository.AssignmentRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;

    @Transactional
    public AssignmentResponse createAssignment(AssignmentRequest dto) {
        Assignment assignment = Assignment.createAssignment(dto.title(), dto.score(), dto.description());
        assignmentRepository.save(assignment);
        return AssignmentResponse.from(assignment);
    }
}
