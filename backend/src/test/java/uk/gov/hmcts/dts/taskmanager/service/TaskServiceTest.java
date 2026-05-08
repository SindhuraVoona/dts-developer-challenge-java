package uk.gov.hmcts.dts.taskmanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.hmcts.dts.taskmanager.dto.TaskCreateRequest;
import uk.gov.hmcts.dts.taskmanager.dto.TaskResponse;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;
import uk.gov.hmcts.dts.taskmanager.exception.TaskNotFoundException;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskDataGateway taskDataGateway;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskDataGateway);
    }

    @Test
    void shouldCreateTask() {
        TaskCreateRequest request = new TaskCreateRequest(
            "Review file",
            "Review the case file",
            TaskStatus.TODO,
            LocalDateTime.of(2026, 5, 10, 9, 0)
        );
        when(taskDataGateway.createTask(request)).thenReturn(taskResponse(1L, TaskStatus.TODO));

        TaskResponse response = taskService.createTask(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Review file");
        assertThat(response.status()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void shouldReturnAllTasks() {
        when(taskDataGateway.getAllTasks()).thenReturn(List.of(taskResponse(1L, TaskStatus.TODO), taskResponse(2L, TaskStatus.COMPLETED)));

        List<TaskResponse> tasks = taskService.getAllTasks();

        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(1).status()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    void shouldUpdateTaskStatus() {
        when(taskDataGateway.updateTaskStatus(1L, TaskStatus.IN_PROGRESS)).thenReturn(taskResponse(1L, TaskStatus.IN_PROGRESS));

        TaskResponse response = taskService.updateTaskStatus(1L, TaskStatus.IN_PROGRESS);

        assertThat(response.status()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldDeleteTask() {
        taskService.deleteTask(1L);

        verify(taskDataGateway).deleteTask(1L);
    }

    @Test
    void shouldThrowWhenTaskIsMissing() {
        when(taskDataGateway.getTaskById(99L)).thenThrow(new TaskNotFoundException(99L));

        assertThatThrownBy(() -> taskService.getTaskById(99L))
            .isInstanceOf(TaskNotFoundException.class)
            .hasMessage("Task with id 99 was not found");
    }

    private TaskResponse taskResponse(Long id, TaskStatus status) {
        return new TaskResponse(
            id,
            "Review file",
            "Review the case file",
            status,
            LocalDateTime.of(2026, 5, 10, 9, 0),
            LocalDateTime.of(2026, 5, 1, 9, 0),
            LocalDateTime.of(2026, 5, 1, 9, 30)
        );
    }
}
