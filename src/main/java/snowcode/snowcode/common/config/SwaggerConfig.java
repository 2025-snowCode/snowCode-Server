package snowcode.snowcode.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
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
        return (operation, handlerMethod) -> {
            this.addSchema(operation);
            return operation;
        };
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer(OperationCustomizer myOperationCustomizer) {
        return openApi -> openApi.getPaths().forEach((path, pathItem) ->
                pathItem.readOperations().forEach(operation -> {
                    HandlerMethod handlerMethod = null; // 필요 없으면 null
                    myOperationCustomizer.customize(operation, handlerMethod);
                })
        );
    }



    private void addSchema(Operation operation) {
        final Content content = operation.getResponses().get("200").getContent();
        if (content != null) {
            content.forEach((mediaTypeKey, mediaType) -> {
                Schema<?> originalSchema = mediaType.getSchema();
                Schema<?> wrappedSchema = wrapSchema(originalSchema);
                mediaType.setSchema(wrappedSchema);
            });
        }
    }

    private Schema<?> wrapSchema(Schema<?> originalSchema) {
        final Schema<?> wrapperSchema = new Schema<>();

        wrapperSchema.addProperty("success", new Schema<>().type("boolean").example(true));
        wrapperSchema.addProperty("response", originalSchema);

        return wrapperSchema;
    }
}