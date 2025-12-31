package snowcode.snowcode.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snowcode.snowcode.auth.dto.login.LoginRequest;
import snowcode.snowcode.auth.dto.login.SignUpResponse;
import snowcode.snowcode.auth.dto.login.SignUpWithCookieResponse;
import snowcode.snowcode.auth.service.AuthService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;

import java.security.GeneralSecurityException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2/authorization")
@SecurityRequirements
@Tag(name = "소셜 로그인", description = "Login API")
public class LoginController {

    private final AuthService authService;

    @PostMapping
    @Operation(summary = "로그인 API", description = "카카오")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인에 성공하였습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignUpResponse.class)
                    )),
            @ApiResponse(responseCode = "400", description = """
                    [UNAUTHORIZED] : 접근 권한이 없습니다. <br>
                    [INVALID_PROVIDER] : provider은 KAKAO 이어야 합니다.
                    [PROVIDER_MISMATCH] : provider이 일치하지 않습니다.
                    """,
                    content = {@Content(mediaType = "application/json")}),
    })
    public BasicResponse<SignUpResponse> login(HttpServletResponse response, @Valid @RequestBody LoginRequest dto) throws GeneralSecurityException {
        SignUpWithCookieResponse withCookieResponse = authService.login(dto);

        // 쿠키 추가
        response.addHeader(HttpHeaders.SET_COOKIE, withCookieResponse.accessCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, withCookieResponse.refreshCookie().toString());

        SignUpResponse member = SignUpResponse.of(withCookieResponse.member(), withCookieResponse.accessToken());
        return ResponseUtil.success(member);
    }
}
