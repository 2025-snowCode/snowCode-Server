package snowcode.snowcode.code.controller;

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
    public BasicResponse<CodeResponse> findCode(@PathVariable Long codeId) {
        CodeResponse code = codeService.findCode(codeId);
        return ResponseUtil.success(code);
    }
}
