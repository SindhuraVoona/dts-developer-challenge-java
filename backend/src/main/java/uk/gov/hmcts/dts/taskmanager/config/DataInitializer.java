package uk.gov.hmcts.dts.taskmanager.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.gov.hmcts.dts.taskmanager.entity.TaskEntity;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;
import uk.gov.hmcts.dts.taskmanager.repository.TaskRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner preloadTasks(TaskRepository taskRepository) {
        return args -> {
            if (taskRepository.count() > 0) {
                return;
            }

            taskRepository.save(buildTask(
                "Review case bundle",
                "Check documents before the afternoon hearing.",
                TaskStatus.TODO,
                LocalDateTime.now().plusDays(1)
            ));

            taskRepository.save(buildTask(
                "Call applicant representative",
                "Confirm missing evidence has been received.",
                TaskStatus.IN_PROGRESS,
                LocalDateTime.now().plusHours(6)
            ));

            taskRepository.save(buildTask(
                "Archive completed appeal",
                "Move final documents into the completed folder.",
                TaskStatus.COMPLETED,
                LocalDateTime.now().minusHours(2)
            ));
        };
    }

    private TaskEntity buildTask(String title, String description, TaskStatus status, LocalDateTime dueDateTime) {
        TaskEntity task = new TaskEntity();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setDueDateTime(dueDateTime);
        return task;
    }
}
