package snowcode.snowcode.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("snowCode Server API")
                .description("swagger description")
                .version("1.0.0");
    }


    @Bean
    public OperationCustomizer operationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            addResponseBodyWrapperSchemaExample(operation);
            return operation;
        };
    }

    private void addResponseBodyWrapperSchemaExample(Operation operation) {
        if (operation.getResponses() != null
                && operation.getResponses().get("200") != null
                && operation.getResponses().get("200").getContent() != null) {

            Content content = operation.getResponses().get("200").getContent();

            content.forEach((mediaTypeKey, mediaType) -> {
                Schema<?> originalSchema = mediaType.getSchema();
                if (originalSchema != null) {
                    Schema<?> wrapperSchema = wrapSchema(originalSchema);
                    mediaType.setSchema(wrapperSchema);
                }
            });
        }
    }

    private Schema<?> wrapSchema(Schema<?> originalSchema) {
        Schema<Object> wrapperSchema = new Schema<>();

        wrapperSchema.addProperty("status",
                new Schema<>().type("string").example("OK"));
        wrapperSchema.addProperty("message",
                new Schema<>().type("string").example("OK"));
        wrapperSchema.addProperty("data", originalSchema);

        return wrapperSchema;
    }
}