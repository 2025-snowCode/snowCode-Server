package snowcode.snowcode.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        wrapperSchema.addProperty("responses", originalSchema);

        return wrapperSchema;
    }
}