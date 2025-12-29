package snowcode.snowcode.unit.exception;

import lombok.Getter;

@Getter
public class UnitException extends RuntimeException {
    private UnitErrorCode code;
    private String message;

    public UnitException(UnitErrorCode code) {
        super();
        this.code = code;
        this.message = code.getMessage();
    }

    public UnitException(UnitErrorCode code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
