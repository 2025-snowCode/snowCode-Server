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
import snowcode.snowcode.submission.dto.JudgeResultDto;
import snowcode.snowcode.submission.dto.SubmissionListResponse;
import snowcode.snowcode.submission.dto.SubmissionResponse;
import snowcode.snowcode.submission.dto.SubmissionWithCode;
import snowcode.snowcode.testcase.dto.TestcaseInfoResponse;
import snowcode.snowcode.testcase.service.TestcaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionWithCodeFacade {

    private final SubmissionService submissionService;
    private final CodeService codeService;
    private final CodeExecutionService codeExecutionService;
    private final TestcaseService testcaseService;

    public SubmissionResponse createSubmissionWithCode(Member member, Assignment assignment, AssignmentRegistration assignmentRegistration, CodeRequest dto) {
        // 채점 준비 (과제, 테스트케이스 불러오기)
        List<TestcaseInfoResponse> testcaseList = testcaseService.findByTestcases(assignment.getId());

        // 채점
        JudgeResultDto judgeDto = codeExecutionService.judgeSubmission(testcaseList, dto.code(), assignment.getScore());

        // 저장 로직 (다 맞았으면 totalScore, 그렇지 않은 경우 0점 부여)
        Submission submission = submissionService.createSubmission(member, assignmentRegistration, judgeDto.totalCount() == judgeDto.passCount() ? judgeDto.totalScore() : 0 );
        Long codeId = codeService.createCode(submission, dto).id();

        return SubmissionResponse.of(codeId, judgeDto);

    }

    public SubmissionListResponse findSubmissionList(Assignment assignment, AssignmentRegistration ar) {
        int originalScore = assignment.getScore();

        List<Long> submissionIds = submissionService.findAllByRegistrationId(ar.getId());
        List<SubmissionWithCode> dtoList = new ArrayList<>();

        for (Long submissionId : submissionIds) {
            Optional<Submission> submission = submissionService.findById(submissionId);
            if (submission.isEmpty()) break;
            Long codeId = codeService.findBySubmissionId(submissionId);
            SubmissionWithCode dto = SubmissionWithCode.of(codeId, originalScore, submission.get());
            dtoList.add(dto);
        }

        return new SubmissionListResponse(assignment.getId(), dtoList);
    }

    public void deleteSubmissionWithRegistrationId(Long registrationId) {
        List<Long> submissionIds = submissionService.findAllByRegistrationId(registrationId);
        codeService.deleteAllBySubmissionIdIn(submissionIds);
        submissionService.deleteAllById(submissionIds);
    }
}
