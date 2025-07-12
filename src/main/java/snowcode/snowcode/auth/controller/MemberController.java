package snowcode.snowcode.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.auth.dto.MemberRequest;
import snowcode.snowcode.auth.dto.MemberResponse;
import snowcode.snowcode.auth.service.MemberService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public BasicResponse<MemberResponse> signup (@RequestBody MemberRequest rq) {
        MemberResponse memberResponse = memberService.signup(rq);
        return ResponseUtil.success(memberResponse);
    }

    @GetMapping("/me/{memberId}")
    public BasicResponse<MemberResponse> findMember(@PathVariable long memberId) {
        MemberResponse memberResponse = memberService.findMember(memberId);
        return ResponseUtil.success(memberResponse);
    }
}
