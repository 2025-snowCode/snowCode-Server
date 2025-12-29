package snowcode.snowcode.common.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ErrorEntity;
import snowcode.snowcode.common.response.ResponseUtil;

import java.io.IOException;

@Slf4j(topic = "FORBIDDEN_EXCEPTION_HANDLER")
@AllArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        log.error("No Authorities", accessDeniedException);

        // ErrorEntity 생성
        ErrorEntity errorEntity = new ErrorEntity(
                HttpStatus.FORBIDDEN.name(),
                "접근 권한이 없습니다."
        );

        // BaseResponse로 감싸기
        BasicResponse<ErrorEntity> errorResponse = ResponseUtil.error(errorEntity);

        // JSON 변환
        String responseBody = objectMapper.writeValueAsString(errorResponse);

        // 응답 작성
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}
