package snowcode.snowcode.submission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.code.dto.CodeRequest;
import snowcode.snowcode.code.service.CodeService;
import snowcode.snowcode.submission.domain.Submission;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionWithCodeFacade {

    private final SubmissionService submissionService;
    private final CodeService codeService;

    public Submission createSubmissionWithCode(Member member, AssignmentRegistration assignmentRegistration, CodeRequest dto) {
        Submission submission = submissionService.createSubmission(member, assignmentRegistration);
        codeService.createCode(submission, dto);
        return submission;
    }

    public void deleteSubmissionWithRegistrationId(Long registrationId) {
        List<Long> submissionIds = submissionService.findAllByRegistrationId(registrationId);
        codeService.deleteAllBySubmissionIdIn(submissionIds);
        submissionService.deleteAllById(submissionIds);
    }
}
