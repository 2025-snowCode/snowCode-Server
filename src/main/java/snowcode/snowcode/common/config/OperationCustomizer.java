package snowcode.snowcode.common.config;

import io.swagger.v3.oas.models.Operation;
import org.springframework.web.method.HandlerMethod;

@FunctionalInterface
public interface OperationCustomizer {
    Operation customize(Operation operation, HandlerMethod handlerMethod);
}
