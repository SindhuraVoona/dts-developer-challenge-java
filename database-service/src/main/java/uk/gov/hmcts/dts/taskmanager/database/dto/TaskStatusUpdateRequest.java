package uk.gov.hmcts.dts.taskmanager.database.dto;

import jakarta.validation.constraints.NotNull;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;

public record TaskStatusUpdateRequest(
    @NotNull(message = "status is required")
    TaskStatus status
) {
}
