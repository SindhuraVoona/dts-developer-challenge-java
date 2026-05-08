package uk.gov.hmcts.dts.taskmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class DatabaseServiceClientConfig {

    @Bean
    RestClient databaseServiceRestClient(RestClient.Builder builder,
                                         @Value("${services.database-service.base-url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }
}
