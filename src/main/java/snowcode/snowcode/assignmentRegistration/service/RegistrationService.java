package snowcode.snowcode.assignmentRegistration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.assignmentRegistration.repository.RegistrationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistrationService {

    private final RegistrationRepository registrationRepository;

    public Map<Long, Integer> countAssignmentsByCourseId(List<Long> courseIds) {
        List<Object[]> results = registrationRepository.countAssignmentsByCourseIds(courseIds);

        return results.stream()
                        .collect(Collectors.toMap(
                                row -> (Long) row[0],
                                row -> ((Long) row[1]).intValue()
                        ));
    }

    public List<Assignment> findAllByUnitId(Long unitId) {
        List<AssignmentRegistration> registrations = registrationRepository.findAllByUnitId(unitId);

        List<Assignment> assignments = new ArrayList<>();

        for (AssignmentRegistration registration : registrations) {
            assignments.add(registration.getAssignment());
        }

        return assignments;
    }

    @Transactional
    public void deleteAllByAssignmentId(Long assignmentId) {
        registrationRepository.deleteAllByAssignmentId(assignmentId);
    }
}
