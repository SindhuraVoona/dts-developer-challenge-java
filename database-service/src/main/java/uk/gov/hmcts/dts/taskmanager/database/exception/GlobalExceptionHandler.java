package uk.gov.hmcts.dts.taskmanager.database.exception;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import uk.gov.hmcts.dts.taskmanager.database.dto.ErrorResponse;
import uk.gov.hmcts.dts.taskmanager.database.dto.ValidationError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException exception,
                                                            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI(), List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception,
                                                          HttpServletRequest request) {
        List<ValidationError> validationErrors = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
            .toList();

        return ResponseEntity.badRequest()
            .body(buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI(), validationErrors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", request.getRequestURI(), List.of()));
    }

    private ErrorResponse buildErrorResponse(HttpStatus status,
                                             String message,
                                             String path,
                                             List<ValidationError> validationErrors) {
        return new ErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            path,
            validationErrors
        );
    }
}
