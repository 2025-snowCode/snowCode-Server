package snowcode.snowcode.submission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignmentRegistration.domain.AssignmentRegistration;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.code.dto.CodeRequest;
import snowcode.snowcode.code.service.CodeExecutionService;
import snowcode.snowcode.code.service.CodeService;
import snowcode.snowcode.submission.domain.Submission;
import snowcode.snowcode.submission.exception.SubmissionErrorCode;
import snowcode.snowcode.submission.exception.SubmissionException;
import snowcode.snowcode.testcase.dto.TestcaseInfoResponse;
import snowcode.snowcode.testcase.service.TestcaseService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionWithCodeFacade {

    private final SubmissionService submissionService;
    private final CodeService codeService;
    private final CodeExecutionService codeExecutionService;
    private final TestcaseService testcaseService;

    public Submission createSubmissionWithCode(Member member, AssignmentRegistration assignmentRegistration, CodeRequest dto) {
        try {
            String result = codeExecutionService.run(dto.code()).get();
            // 채점
            Assignment assignment = assignmentRegistration.getAssignment();
            List<TestcaseInfoResponse> testcaseList = testcaseService.findByTestcases(assignment.getId());
            int score = codeExecutionService.judgeSubmission(testcaseList, result, assignment.getScore());
            Submission submission = submissionService.createSubmission(member, assignmentRegistration, score);
            codeService.createCode(submission, dto);
            return submission;

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new SubmissionException(SubmissionErrorCode.FILE_NOT_FOUND);
        } catch (IOException ex) {
            throw new SubmissionException(SubmissionErrorCode.FILE_NOT_FOUND);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteSubmissionWithRegistrationId(Long registrationId) {
        List<Long> submissionIds = submissionService.findAllByRegistrationId(registrationId);
        codeService.deleteAllBySubmissionIdIn(submissionIds);
        submissionService.deleteAllById(submissionIds);
    }
}
