package snowcode.snowcode.common.response;

public class ResponseUtil {

    public static <T> BasicResponse<T> success (T response) { return new BasicResponse<>(true, response); }
    public static BasicResponse<ErrorEntity> error (ErrorEntity e) { return new BasicResponse<>(false, e); }
}
