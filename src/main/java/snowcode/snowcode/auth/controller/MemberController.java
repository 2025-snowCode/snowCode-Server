package snowcode.snowcode.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입 API", description = "회원가입 (임시)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_INPUT",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "400", description = "role에 USER, ADMIN이 아닌 다른 것을 넣은 경우",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<MemberResponse> signup (@Valid @RequestBody MemberRequest rq) {
        MemberResponse memberResponse = memberService.signup(rq);
        return ResponseUtil.success(memberResponse);
    }

    @GetMapping("/me/{memberId}")
    public BasicResponse<MyProfileResponse> findMember(@Schema(description = "memberId", example = "1")
                                                           @PathVariable Long memberId) {
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
