package uk.gov.hmcts.dts.taskmanager.service;

import java.util.List;
import java.util.Objects;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import uk.gov.hmcts.dts.taskmanager.dto.TaskCreateRequest;
import uk.gov.hmcts.dts.taskmanager.dto.TaskResponse;
import uk.gov.hmcts.dts.taskmanager.dto.TaskStatusUpdateRequest;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;
import uk.gov.hmcts.dts.taskmanager.exception.ServiceUnavailableException;
import uk.gov.hmcts.dts.taskmanager.exception.TaskNotFoundException;

@Component
public class DatabaseServiceTaskDataGateway implements TaskDataGateway {

    private static final String CB_NAME = "database-service";

    private static final ParameterizedTypeReference<List<TaskResponse>> TASK_LIST_TYPE =
        new ParameterizedTypeReference<>() { };

    private final RestClient databaseServiceRestClient;

    public DatabaseServiceTaskDataGateway(RestClient databaseServiceRestClient) {
        this.databaseServiceRestClient = databaseServiceRestClient;
    }

    @Override
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "createTaskFallback")
    public TaskResponse createTask(TaskCreateRequest request) {
        return requireBody(databaseServiceRestClient.post()
            .uri("/internal/tasks")
            .body(request)
            .retrieve()
            .body(TaskResponse.class));
    }

    @Override
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "getTaskByIdFallback")
    public TaskResponse getTaskById(Long id) {
        try {
            return requireBody(databaseServiceRestClient.get()
                .uri("/internal/tasks/{id}", id)
                .retrieve()
                .body(TaskResponse.class));
        } catch (RestClientResponseException exception) {
            throw mapException(id, exception);
        }
    }

    @Override
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "getAllTasksFallback")
    public List<TaskResponse> getAllTasks() {
        return requireBody(databaseServiceRestClient.get()
            .uri("/internal/tasks")
            .retrieve()
            .body(TASK_LIST_TYPE));
    }

    @Override
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "updateTaskStatusFallback")
    public TaskResponse updateTaskStatus(Long id, TaskStatus status) {
        try {
            return requireBody(databaseServiceRestClient.patch()
                .uri("/internal/tasks/{id}/status", id)
                .body(new TaskStatusUpdateRequest(status))
                .retrieve()
                .body(TaskResponse.class));
        } catch (RestClientResponseException exception) {
            throw mapException(id, exception);
        }
    }

    @Override
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "deleteTaskFallback")
    public void deleteTask(Long id) {
        try {
            databaseServiceRestClient.delete()
                .uri("/internal/tasks/{id}", id)
                .retrieve()
                .toBodilessEntity();
        } catch (RestClientResponseException exception) {
            throw mapException(id, exception);
        }
    }

    // ── Fallbacks ────────────────────────────────────────────────────────────
    // Called when the circuit is OPEN or a non-ignored exception is thrown.
    // TaskNotFoundException is in ignoreExceptions so it always propagates normally.

    @SuppressWarnings("unused")
    private TaskResponse createTaskFallback(TaskCreateRequest request, Throwable cause) {
        throw new ServiceUnavailableException("database-service is unavailable: cannot create task", cause);
    }

    @SuppressWarnings("unused")
    private TaskResponse getTaskByIdFallback(Long id, Throwable cause) {
        throw new ServiceUnavailableException("database-service is unavailable: cannot retrieve task " + id, cause);
    }

    @SuppressWarnings("unused")
    private List<TaskResponse> getAllTasksFallback(Throwable cause) {
        // Graceful degradation — return empty list rather than failing hard
        return List.of();
    }

    @SuppressWarnings("unused")
    private TaskResponse updateTaskStatusFallback(Long id, TaskStatus status, Throwable cause) {
        throw new ServiceUnavailableException("database-service is unavailable: cannot update task " + id, cause);
    }

    @SuppressWarnings("unused")
    private void deleteTaskFallback(Long id, Throwable cause) {
        throw new ServiceUnavailableException("database-service is unavailable: cannot delete task " + id, cause);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private static <T> T requireBody(T body) {
        return Objects.requireNonNull(body, "database-service returned an empty response body");
    }

    private static RuntimeException mapException(Long id, RestClientResponseException exception) {
        if (exception.getStatusCode().value() == 404) {
            return new TaskNotFoundException(id);
        }
        return exception;
    }
}
