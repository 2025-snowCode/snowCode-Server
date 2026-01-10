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
import snowcode.snowcode.submission.dto.SubmissionResponse;
import snowcode.snowcode.testcase.dto.TestcaseInfoResponse;
import snowcode.snowcode.testcase.service.TestcaseService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionWithCodeFacade {

    private final SubmissionService submissionService;
    private final CodeService codeService;
    private final CodeExecutionService codeExecutionService;
    private final TestcaseService testcaseService;

    public SubmissionResponse createSubmissionWithCode(Member member, AssignmentRegistration assignmentRegistration, CodeRequest dto) {

        // 채점 준비 (과제, 테스트케이스 불러오기)
        Assignment assignment = assignmentRegistration.getAssignment();
        List<TestcaseInfoResponse> testcaseList = testcaseService.findByTestcases(assignment.getId());

        // 채점
        int score = codeExecutionService.judgeSubmission(testcaseList, dto.code(), assignment.getScore());

        // 저장 로직
        Submission submission = submissionService.createSubmission(member, assignmentRegistration, score);
        Long codeId = codeService.createCode(submission, dto).id();
        return SubmissionResponse.of(codeId, submission);
    }

    public void deleteSubmissionWithRegistrationId(Long registrationId) {
        List<Long> submissionIds = submissionService.findAllByRegistrationId(registrationId);
        codeService.deleteAllBySubmissionIdIn(submissionIds);
        submissionService.deleteAllById(submissionIds);
    }
}
