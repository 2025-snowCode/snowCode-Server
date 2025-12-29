package snowcode.snowcode.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snowcode.snowcode.auth.dto.login.LoginRequest;
import snowcode.snowcode.auth.dto.login.ReissueRequest;
import snowcode.snowcode.auth.dto.login.SignUpResponse;
import snowcode.snowcode.auth.dto.login.TokenResponse;
import snowcode.snowcode.auth.service.AuthService;
import snowcode.snowcode.auth.service.TokenService;
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
    private final TokenService tokenService;

    @PostMapping
    @Operation(summary = "로그인 API", description = "카카오/구글/애플")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인에 성공하였습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignUpResponse.class)
                    )),
            @ApiResponse(responseCode = "400", description = """
                    [UNAUTHORIZED] : 접근 권한이 없습니다. <br>
                    [INVALID_PROVIDER] : provider은 KAKAO or GOOGLE or APPLE 이어야 합니다.
                    [PROVIDER_MISMATCH] : provider이 일치하지 않습니다.
                    """,
                    content = {@Content(mediaType = "application/json")}),
    })
    public BasicResponse<SignUpResponse> login(@Valid @RequestBody LoginRequest dto) throws GeneralSecurityException {
        SignUpResponse member = authService.login(dto);
        return ResponseUtil.success(member);
    }

    // 액세스 토큰 재발급
    @PostMapping("/auth/reissue")
    @Operation(summary = "Access Token 재발급 API", description = "Access Token 만료 시 재발급 가능한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token 재발급에 성공하였습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class)
                    )),
            @ApiResponse(responseCode = "400", description = "[UNAUTHORIZED] : 접근 권한이 없습니다.<br>" +
                    "[MEMBER_NOT_FOUND] : 회원을 찾을 수 없습니다. <br>" +
                    "[NOT_FOUND_TOKEN] : 토큰을 찾을 수 없습니다. <br>" +
                    "[INVALID_TOKEN] : 유효하지 않은 토큰입니다. <br>" +
                    "[TOKEN_HAS_EXPIRED] : 토큰의 유효기간이 만료되었습니다. (refresh Token 유효기간 만료) <br>",
                    content = {@Content(mediaType = "application/json")})
    })
    public BasicResponse<TokenResponse> refreshAccessToken(@Valid @RequestBody ReissueRequest dto) {
        authService.loadMember();
        TokenResponse tokenResponse = tokenService.refresh(dto.refreshToken());
        return ResponseUtil.success(tokenResponse);
    }
}
