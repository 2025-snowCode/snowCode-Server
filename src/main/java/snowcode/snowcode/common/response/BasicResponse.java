package snowcode.snowcode.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BasicResponse<T> {
    private boolean success;
    private T response;
}
