package snowcode.snowcode.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "공통 응답 wrapper")
public class BasicResponse<T> {

    @Schema(description = "성공 여부")
    private boolean success;

    @Schema(description = "메시지")
    private T response;
}
