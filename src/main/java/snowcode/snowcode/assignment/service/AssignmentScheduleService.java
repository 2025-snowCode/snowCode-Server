package snowcode.snowcode.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.dto.AssignmentScheduleDetailResponse;
import snowcode.snowcode.assignment.dto.AssignmentScheduleResponse;
import snowcode.snowcode.assignment.dto.AssignmentUpcomingDateResponse;
import snowcode.snowcode.assignment.repository.AssignmentRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentScheduleService {
    private final AssignmentRepository assignmentRepository;

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
