package uk.gov.hmcts.dts.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;

@Schema(description = "Payload for updating a task's status")
public record TaskStatusUpdateRequest(
    @Schema(description = "New status for the task", example = "IN_PROGRESS")
    @NotNull(message = "status is required")
    TaskStatus status
) {
}
