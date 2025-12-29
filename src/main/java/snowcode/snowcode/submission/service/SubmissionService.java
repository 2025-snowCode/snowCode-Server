package snowcode.snowcode.submission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.submission.domain.Submission;
import snowcode.snowcode.submission.dto.SubmissionScore;
import snowcode.snowcode.submission.repository.SubmissionRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public Submission createSubmission(Member member, AssignmentRegistration assignmentRegistration) {
        Submission submission = Submission.createSubmission(0, member, assignmentRegistration);
        submissionRepository.save(submission);
        return submission;
    }

    @Transactional(readOnly = true)
    public List<Long> findAllByRegistrationId(Long registrationId) {
        return submissionRepository.findIdsByRegistrationId(registrationId);
    }

    @Transactional(readOnly = true)
    public List<Submission> findByMemberIdAndAssignmentRegistrationId(Long memberId, Long regId) {
        return submissionRepository.findByMemberIdAndAssignmentRegistrationId(memberId, regId);
    }

    @Transactional(readOnly = true)
    public List<SubmissionScore> findMaxScoreByMemberIdsAndRegsIds(List<Long> memberIds, List<Long> regIds) {
        return submissionRepository.findMaxScoresByMemberIdsAndRegistrationIds(memberIds, regIds);
    }

    public void deleteAllById(List<Long> submissionIds) {
        submissionRepository.deleteAllById(submissionIds);
    }
}
