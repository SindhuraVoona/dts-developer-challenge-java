package uk.gov.hmcts.dts.taskmanager.database.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;

public record TaskCreateRequest(
    @NotBlank(message = "title is required")
    String title,
    String description,
    @NotNull(message = "status is required")
    TaskStatus status,
    @NotNull(message = "dueDateTime is required")
    LocalDateTime dueDateTime
) {
}
