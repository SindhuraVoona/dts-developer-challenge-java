package uk.gov.hmcts.dts.taskmanager.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;

@Schema(description = "Payload for creating a new task")
public record TaskCreateRequest(
    @Schema(description = "Title of the task", example = "Review case file")
    @NotBlank(message = "title is required")
    String title,

    @Schema(description = "Optional description", example = "Review and summarise the case file by end of day")
    String description,

    @Schema(description = "Initial status of the task", example = "TODO")
    @NotNull(message = "status is required")
    TaskStatus status,

    @Schema(description = "Due date and time in ISO-8601 format", example = "2026-05-10T09:00:00")
    @NotNull(message = "dueDateTime is required")
    LocalDateTime dueDateTime
) {
}
