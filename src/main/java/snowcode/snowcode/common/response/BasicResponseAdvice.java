package snowcode.snowcode.common.response;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = "kr.co.swagger")
public class BasicResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        final HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

        int status = servletResponse.getStatus();
        final HttpStatus resolve = HttpStatus.resolve(status);

        if (resolve == null) return body;

        if (resolve.is2xxSuccessful()) {
            return ResponseUtil.success(body);
        }

//        if (resolve.is4xxClientError()) {
//            return ResponseUtil.error(body); // 바디에서 받아오기 -> 에러 핸들ㄹ러에서 받아오기!
//        }

        return body;
    }
}
