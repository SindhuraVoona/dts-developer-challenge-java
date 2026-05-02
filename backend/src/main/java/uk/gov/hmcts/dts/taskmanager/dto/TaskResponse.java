package uk.gov.hmcts.dts.taskmanager.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;

@Schema(description = "Task resource returned by the API")
public record TaskResponse(
    @Schema(description = "Unique identifier", example = "1") Long id,
    @Schema(description = "Title of the task", example = "Review case file") String title,
    @Schema(description = "Description of the task") String description,
    @Schema(description = "Current status", example = "TODO") TaskStatus status,
    @Schema(description = "Due date and time") LocalDateTime dueDateTime,
    @Schema(description = "Time the task was created") LocalDateTime createdAt,
    @Schema(description = "Time the task was last updated") LocalDateTime updatedAt
) {
}
