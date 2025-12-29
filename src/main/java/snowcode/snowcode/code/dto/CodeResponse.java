package snowcode.snowcode.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import snowcode.snowcode.code.domain.Code;

public record CodeResponse(
        @Schema(description = "code id", example = "1")
        Long id,
        @Schema(description = "code", example = "print(\"hello\");")
        String code,
        @Schema(description = "언어", example = "PYTHON")
        String language) {

    public static CodeResponse from (Code code) {
        return new CodeResponse(code.getId(), code.getCode(), code.getLanguage().toString());
    }
}
