package snowcode.snowcode.code.dto;

import snowcode.snowcode.code.domain.Code;

public record CodeResponse(Long id, String code, String language) {

    public static CodeResponse from (Code code) {
        return new CodeResponse(code.getId(), code.getCode(), code.getLanguage().toString());
    }
}
