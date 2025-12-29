package snowcode.snowcode.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${cors.allowed.frontend}")
    private String frontend;

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components)
                .servers(List.of(
                        new Server()
                                .url(frontend)
                                .description("Production 서버")
                ));
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