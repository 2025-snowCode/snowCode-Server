package snowcode.snowcode.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.dto.*;
import snowcode.snowcode.assignment.exception.AssignmentErrorCode;
import snowcode.snowcode.assignment.exception.AssignmentException;
import snowcode.snowcode.assignment.repository.AssignmentRepository;
import snowcode.snowcode.unit.domain.Unit;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
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


    public AssignmentScheduleResponse listUpMySchedule(Long memberId) {
        List<AssignmentUpcomingDateResponse> dtoList = scheduleAssignment(7, memberId);
        return new AssignmentScheduleResponse(dtoList.size(), dtoList);
    }


    public List<AssignmentUpcomingDateResponse> scheduleAssignment(int upcomingDay, Long memberId) {
        List<Object[]> result = fetchUnsubmittedAssignments(upcomingDay, memberId);
        Map<LocalDate, List<AssignmentScheduleDetailResponse>> scheduleDetailDtoMap = groupAssignmentsByDueDate(result);

        List<AssignmentUpcomingDateResponse> dtoList = new ArrayList<>();

        for (Map.Entry<LocalDate, List<AssignmentScheduleDetailResponse>> entry : scheduleDetailDtoMap.entrySet()) {
            LocalDate date = entry.getKey();
            List<AssignmentScheduleDetailResponse> dto = entry.getValue();
            dtoList.add(new AssignmentUpcomingDateResponse(date.toString(), computeRemainingDate(date), dto));
        }
        return dtoList;
    }


    private List<Object[]> fetchUnsubmittedAssignments(int upcomingDay, Long memberId) {
        return assignmentRepository.findUnsubmittedAssignmentsWithinWeek(memberId, LocalDate.now(), LocalDate.now().plusDays(upcomingDay));
    }

    private Map<LocalDate, List<AssignmentScheduleDetailResponse>> groupAssignmentsByDueDate(List<Object[]> result) {

        Map<LocalDate, List<AssignmentScheduleDetailResponse>> group = new TreeMap<>();
        for (Object[] row : result) {
            String courseName = (String) row[0];
            String section = (String) row[1];
            String assignmentName = (String) row[2];
            LocalDate dueDate = (LocalDate) row[3];

            AssignmentScheduleDetailResponse detailDto = new AssignmentScheduleDetailResponse(courseName, section, assignmentName);

            if (!group.containsKey(dueDate)) {
                group.put(dueDate, new ArrayList<>());
            }
            group.get(dueDate).add(detailDto);
        }

        return group;
    }

    private int computeRemainingDate(LocalDate dueDate) {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }
}
