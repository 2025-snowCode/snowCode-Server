package snowcode.snowcode.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snowcode.snowcode.code.dto.CodeResponse;
import snowcode.snowcode.code.service.CodeService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/code")
public class CodeController {
    private final CodeService codeService;

    @GetMapping("{codeId}")
    @Operation(summary = "코드 조회 API", description = "코드 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코드 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CodeResponse.class))}),
            @ApiResponse(responseCode = "404", description = "코드를 찾을 수 없습니다.",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<CodeResponse> findCode(@PathVariable Long codeId) {
        CodeResponse code = codeService.findCode(codeId);
        return ResponseUtil.success(code);
    }
}
