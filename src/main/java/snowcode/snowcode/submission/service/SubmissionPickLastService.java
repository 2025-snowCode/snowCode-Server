package snowcode.snowcode.submission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.submission.domain.Submission;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionPickLastService implements SubmissionScoreService {

    private final SubmissionService submissionService;

    @Transactional(readOnly = true)
    public Optional<Submission> isSubmitted(Long memberId, AssignmentRegistration registration) {
        List<Submission> submissions = submissionService.findByMemberIdAndAssignmentRegistrationId(memberId, registration.getId());

        if (submissions.isEmpty()) return Optional.empty();
        int fullScore = registration.getAssignment().getScore();

        return submissions.stream()
                .filter(s -> s.getScore() == fullScore)
                .max(Comparator.comparing(Submission::getSubmittedAt))
                .or(() -> submissions.stream().max(Comparator.comparing(Submission::getSubmittedAt)));
    }

    @Transactional(readOnly = true)
    public Map<Long, Map<Long, Integer>> loadScores(
            List<Member> members,
            Map<Long, List<AssignmentRegistration>> regsByUnit) {

        Map<Long, Map<Long, Integer>> result = new HashMap<>();

        for (Member member : members) {
            Map<Long, Integer> regScores = new HashMap<>();

            for (AssignmentRegistration reg : regsByUnit.values().stream().flatMap(List::stream).toList()) {
                List<Submission> submissions = submissionService.findByMemberIdAndAssignmentRegistrationId(member.getId(), reg.getId());
                if (submissions.isEmpty()) continue;

                int fullScore = reg.getAssignment().getScore();

                Optional<Submission> best = submissions.stream()
                        .filter(s -> s.getScore() == fullScore)
                        .max(Comparator.comparing(Submission::getSubmittedAt));

                if (best.isEmpty()) {
                    best = submissions.stream()
                            .max(Comparator.comparing(Submission::getSubmittedAt));
                }

                best.ifPresent(b -> regScores.put(reg.getId(), b.getScore()));
            }

            result.put(member.getId(), regScores);
        }

        return result;
    }
}
