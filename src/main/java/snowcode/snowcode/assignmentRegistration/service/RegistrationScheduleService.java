package snowcode.snowcode.assignmentRegistration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignmentRegistration.dto.RegistrationScheduleDetailResponse;
import snowcode.snowcode.assignmentRegistration.dto.RegistrationScheduleResponse;
import snowcode.snowcode.assignmentRegistration.dto.RegistrationUpcomingDateResponse;
import snowcode.snowcode.assignmentRegistration.repository.RegistrationRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistrationScheduleService {
    private final RegistrationRepository registrationRepository;

    public RegistrationScheduleResponse listUpMySchedule(Long memberId) {
        List<RegistrationUpcomingDateResponse> dtoList = scheduleAssignment(7, memberId);
        return new RegistrationScheduleResponse(dtoList.size(), dtoList);
    }


    public List<RegistrationUpcomingDateResponse> scheduleAssignment(int upcomingDay, Long memberId) {
        List<Object[]> result = fetchUnsubmittedAssignments(upcomingDay, memberId);
        Map<LocalDate, List<RegistrationScheduleDetailResponse>> scheduleDetailDtoMap = groupAssignmentsByDueDate(result);

        List<RegistrationUpcomingDateResponse> dtoList = new ArrayList<>();

        for (Map.Entry<LocalDate, List<RegistrationScheduleDetailResponse>> entry : scheduleDetailDtoMap.entrySet()) {
            LocalDate date = entry.getKey();
            List<RegistrationScheduleDetailResponse> dto = entry.getValue();
            dtoList.add(new RegistrationUpcomingDateResponse(date.toString(), computeRemainingDate(date), dto));
        }
        return dtoList;
    }


    private List<Object[]> fetchUnsubmittedAssignments(int upcomingDay, Long memberId) {
        return registrationRepository.findUnsubmittedAssignmentsWithinWeek(memberId, LocalDate.now(), LocalDate.now().plusDays(upcomingDay));
    }

    private Map<LocalDate, List<RegistrationScheduleDetailResponse>> groupAssignmentsByDueDate(List<Object[]> result) {

        Map<LocalDate, List<RegistrationScheduleDetailResponse>> group = new TreeMap<>();
        for (Object[] row : result) {
            String courseName = (String) row[0];
            String section = (String) row[1];
            String assignmentName = (String) row[2];
            LocalDate dueDate = (LocalDate) row[3];

            RegistrationScheduleDetailResponse detailDto = new RegistrationScheduleDetailResponse(courseName, section, assignmentName);

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
