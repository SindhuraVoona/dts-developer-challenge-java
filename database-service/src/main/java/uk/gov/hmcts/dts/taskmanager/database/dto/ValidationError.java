package uk.gov.hmcts.dts.taskmanager.database.dto;

public record ValidationError(String field, String message) {
}
