package snowcode.snowcode.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.dto.*;
import snowcode.snowcode.auth.service.MemberService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public BasicResponse<MemberResponse> signup (@Valid @RequestBody MemberRequest rq) {
        MemberResponse memberResponse = memberService.signup(rq);
        return ResponseUtil.success(memberResponse);
    }

    @GetMapping("/me/{memberId}")
    public BasicResponse<MyProfileResponse> findMember(@PathVariable Long memberId) {
        MyProfileResponse myProfileResponse = memberService.findMemberById(memberId);
        return ResponseUtil.success(myProfileResponse);
    }

    @GetMapping
    public BasicResponse<MemberCountListResponse> findAllMember() {
        MemberCountListResponse rp = memberService.findAllMember();
        return ResponseUtil.success(rp);
    }

    @PostMapping("/{memberId}/students")
    public BasicResponse<AddProfileResponse> updateStudentId(@PathVariable Long memberId, @Valid @RequestBody AddProfileRequest rq) {
        Member member = memberService.findMember(memberId);
        AddProfileResponse addProfileResponse = memberService.updateStudentId(member, rq.studentId());
        return ResponseUtil.success(addProfileResponse);
    }
}
