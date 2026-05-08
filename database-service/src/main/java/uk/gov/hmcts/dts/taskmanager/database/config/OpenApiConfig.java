package uk.gov.hmcts.dts.taskmanager.database.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI databaseServiceOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Task Manager Database Service API")
                .description("Internal persistence API for task records backed by PostgreSQL")
                .version("v1"));
    }
}
