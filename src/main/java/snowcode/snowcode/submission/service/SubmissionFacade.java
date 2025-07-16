package snowcode.snowcode.submission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.code.dto.CodeRequest;
import snowcode.snowcode.code.service.CodeService;
import snowcode.snowcode.submission.domain.Submission;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionFacade {

    private final SubmissionService submissionService;
    private final CodeService codeService;

    public Submission createSubmissionWithCode(Member member, Assignment assignment, CodeRequest dto) {
        Submission submission = submissionService.createSubmission(member, assignment);
        codeService.createCode(submission, dto);
        return submission;
    }
}
