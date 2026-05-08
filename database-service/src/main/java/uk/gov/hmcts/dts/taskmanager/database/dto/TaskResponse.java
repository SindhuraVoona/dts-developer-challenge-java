package uk.gov.hmcts.dts.taskmanager.database.dto;

import java.time.LocalDateTime;

import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;

public record TaskResponse(
    Long id,
    String title,
    String description,
    TaskStatus status,
    LocalDateTime dueDateTime,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
