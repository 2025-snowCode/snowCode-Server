package snowcode.snowcode.unit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UnitErrorCode {

    UNIT_NOT_FOUND("단원이 존재하지 않습니다.");

    private final String message;
}
