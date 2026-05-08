package uk.gov.hmcts.dts.taskmanager.exception;

/**
 * Thrown when the database-service is unreachable or its circuit breaker is open.
 * Mapped to HTTP 503 by {@link GlobalExceptionHandler}.
 */
public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
