package snowcode.snowcode.code.domain;

import snowcode.snowcode.code.exception.CodeCode;
import snowcode.snowcode.code.exception.CodeException;

import java.util.Arrays;

public enum Language {
    JAVA, PYTHON, C, CPP, JAVASCRIPT;

    public static Language of(String language) {
        return Arrays.stream(Language.values())
                .filter(r -> r.name().equalsIgnoreCase(language))
                .findFirst()
                .orElseThrow(() -> new CodeException(CodeCode.INVALID_LANGUAGE_TYPE));
    }
}
