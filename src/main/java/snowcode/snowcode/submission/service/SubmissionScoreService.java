package snowcode.snowcode.submission.service;

import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.submission.domain.Submission;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SubmissionScoreService {

    Optional<Submission> isSubmitted(Long memberId, AssignmentRegistration registration);
    Map<Long, Map<Long, Integer>> loadScores(List<Member> members, Map<Long, List<AssignmentRegistration>> regsByUnit);

}
