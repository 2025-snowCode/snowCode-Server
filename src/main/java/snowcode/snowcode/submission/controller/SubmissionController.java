package snowcode.snowcode.submission.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.assignment.service.AssignmentService;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.service.MemberService;
import snowcode.snowcode.code.dto.CodeRequest;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;
import snowcode.snowcode.submission.domain.Submission;
import snowcode.snowcode.submission.dto.SubmissionResponse;
import snowcode.snowcode.submission.service.SubmissionWithCodeFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assignments")
public class SubmissionController {
    private final SubmissionWithCodeFacade submissionWithCodeFacade;
    private final AssignmentService assignmentService;
    private final MemberService memberService;

    @PostMapping("/{memberId}/{assignmentId}/code")
    public BasicResponse<SubmissionResponse> createSubmission(@PathVariable Long memberId, @PathVariable Long assignmentId, @RequestBody CodeRequest dto) {
        Member member = memberService.findMember(memberId);
        Assignment assignment = assignmentService.findById(assignmentId);
        Submission submission = submissionWithCodeFacade.createSubmissionWithCode(member, assignment, dto);
        return ResponseUtil.success(SubmissionResponse.of(submission));
    }
}
