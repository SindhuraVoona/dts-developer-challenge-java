package uk.gov.hmcts.dts.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskManagerOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("HMCTS DTS Task Manager API")
                .description("REST API for managing caseworker tasks. Supports creating, retrieving, updating and deleting tasks.")
                .version("v1")
                .contact(new Contact()
                    .name("HMCTS DTS")
                    .url("https://hmcts.github.io"))
                .license(new License()
                    .name("MIT")
                    .url("https://opensource.org/licenses/MIT")));
    }
}
