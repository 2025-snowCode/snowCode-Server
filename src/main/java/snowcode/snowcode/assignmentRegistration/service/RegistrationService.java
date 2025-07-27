package snowcode.snowcode.assignmentRegistration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.assignmentRegistration.repository.RegistrationRepository;
import snowcode.snowcode.unit.domain.Unit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistrationService {

    private final RegistrationRepository registrationRepository;

    @Transactional
    public void createRegistrations(Unit unit, List<Assignment> assignments) {
        List<AssignmentRegistration> registrations = assignments.stream()
                .map(assignment -> AssignmentRegistration.createRegistration(unit, assignment))
                .toList();
        registrationRepository.saveAll(registrations);
    }

    public Map<Long, Integer> countAssignmentsByCourseId(List<Long> courseIds) {
        List<Object[]> results = registrationRepository.countAssignmentsByCourseIds(courseIds);
        return objectToMap(results);
    }

    public List<Assignment> findAllByUnitId(Long unitId) {
        return registrationRepository.findAssignmentsByUnitId(unitId);
    }

    @Transactional
    public void deleteAllByAssignmentId(Long assignmentId) {
        registrationRepository.deleteAllByAssignmentId(assignmentId);
    }

    @Transactional
    public void deleteAllByUnitId(Long unitId) {
        registrationRepository.deleteAllByUnitId(unitId);
    }


    @Transactional
    public void deleteAllByUnitIdIn(List<Long> unitIds) {
        registrationRepository.deleteAllByUnitIdIn(unitIds);
    }

    @Transactional
    public void deleteByUnitIdAndAssignmentId(Long unitId, Long assignmentId) {
        registrationRepository.deleteByUnitIdAndAssignmentId(unitId, assignmentId);
    }

    private Map<Long, Integer> objectToMap(List<Object[]> results) {
         return results.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));
    }
}
