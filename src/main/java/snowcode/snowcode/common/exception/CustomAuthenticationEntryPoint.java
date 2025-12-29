package snowcode.snowcode.common.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ErrorEntity;
import snowcode.snowcode.common.response.ResponseUtil;

import java.io.IOException;

@Slf4j(topic = "UNAUTHORIZATION_EXCEPTION_HANDLER")
@AllArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.error("Not Authenticated Request", authException);

        // 에러 객체 생성
        ErrorEntity errorEntity = new ErrorEntity(
                HttpStatus.UNAUTHORIZED.name(),
                "유효하지 않거나 만료된 토큰입니다."
        );


        BasicResponse<ErrorEntity> errorResponse = ResponseUtil.error(errorEntity);

        // JSON 직렬화
        String responseBody = objectMapper.writeValueAsString(errorResponse);

        // HTTP 응답 설정
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}
