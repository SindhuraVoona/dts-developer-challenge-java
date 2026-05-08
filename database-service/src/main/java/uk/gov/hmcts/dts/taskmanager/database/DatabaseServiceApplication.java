package uk.gov.hmcts.dts.taskmanager.database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("uk.gov.hmcts.dts.taskmanager.entity")
@EnableJpaRepositories("uk.gov.hmcts.dts.taskmanager.repository")
public class DatabaseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseServiceApplication.class, args);
    }
}
