package uk.gov.hmcts.dts.taskmanager.database.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path,
    List<ValidationError> validationErrors
) {
}
