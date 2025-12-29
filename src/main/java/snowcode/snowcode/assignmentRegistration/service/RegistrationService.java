package snowcode.snowcode.assignmentRegistration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.AssignmentSimpleResponse;
import snowcode.snowcode.assignment.exception.AssignmentErrorCode;
import snowcode.snowcode.assignment.exception.AssignmentException;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.assignmentRegistration.repository.RegistrationRepository;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.unit.domain.Unit;

import java.util.ArrayList;
import java.util.HashMap;
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

    public AssignmentRegistration findById(Long registrationId) {
        return registrationRepository.findById(registrationId).orElseThrow(
                () -> new AssignmentException(AssignmentErrorCode.ASSIGNMENT_REGISTRATION_NOT_FOUND)
        );
    }

    public List<AssignmentRegistration> findAllByUnitId(Long unitId) {
        return registrationRepository.findAllByUnitId(unitId);
    }

    public List<AssignmentRegistration> findAllByUnitIdIn(List<Long> unitIds) {
        return registrationRepository.findAllByUnitIdIn(unitIds);
    }

    public List<AssignmentRegistration> findAllByAssignmentId(Long assignmentId) {
        return registrationRepository.findAllByAssignmentId(assignmentId);
    }

    public AssignmentRegistration findByUnitIdAndAssignmentId(Long unitId, Long assignmentId) {
        return registrationRepository.findByUnitIdAndAssignmentId(unitId, assignmentId)
                .orElseThrow(() -> new AssignmentException(AssignmentErrorCode.ASSIGNMENT_REGISTRATION_NOT_FOUND));
    }

    public Map<Course, List<AssignmentSimpleResponse>> findAssignmentsByCourseId(List<Long> courseIds) {
        // FIXME - equals(), hashCode() 오버라이딩 필요
        Map<Course, List<AssignmentSimpleResponse>> courseAssignmentMap = new HashMap<>();

        List<Object[]> findAssignments;

        if(!courseIds.isEmpty()) {
            findAssignments = registrationRepository.findAssignmentsByCourseId(courseIds);
        } else return courseAssignmentMap;

        for (Object[] row : findAssignments) {
            Course course = (Course) row[0];
            Long assignmentId = (Long) row[1];
            String title = (String) row[2];

            courseAssignmentMap
                    .computeIfAbsent(course, k -> new ArrayList<>())
                    .add(new AssignmentSimpleResponse(assignmentId, title));
        }

        return courseAssignmentMap;
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
