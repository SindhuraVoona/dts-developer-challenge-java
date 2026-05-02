package uk.gov.hmcts.dts.taskmanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.hmcts.dts.taskmanager.dto.TaskCreateRequest;
import uk.gov.hmcts.dts.taskmanager.dto.TaskResponse;
import uk.gov.hmcts.dts.taskmanager.entity.TaskEntity;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;
import uk.gov.hmcts.dts.taskmanager.exception.TaskNotFoundException;
import uk.gov.hmcts.dts.taskmanager.mapper.TaskMapper;
import uk.gov.hmcts.dts.taskmanager.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();
        taskService = new TaskService(taskRepository, taskMapper);
    }

    @Test
    void shouldCreateTask() {
        TaskCreateRequest request = new TaskCreateRequest(
            "Review file",
            "Review the case file",
            TaskStatus.TODO,
            LocalDateTime.of(2026, 5, 10, 9, 0)
        );
        TaskEntity saved = taskEntity(1L, TaskStatus.TODO);
        when(taskRepository.save(org.mockito.ArgumentMatchers.any(TaskEntity.class))).thenReturn(saved);

        TaskResponse response = taskService.createTask(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Review file");
        assertThat(response.status()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void shouldReturnAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(taskEntity(1L, TaskStatus.TODO), taskEntity(2L, TaskStatus.COMPLETED)));

        List<TaskResponse> tasks = taskService.getAllTasks();

        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(1).status()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    void shouldUpdateTaskStatus() {
        TaskEntity existing = taskEntity(1L, TaskStatus.TODO);
        TaskEntity updated = taskEntity(1L, TaskStatus.IN_PROGRESS);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(updated);

        TaskResponse response = taskService.updateTaskStatus(1L, TaskStatus.IN_PROGRESS);

        assertThat(response.status()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldDeleteTask() {
        TaskEntity existing = taskEntity(1L, TaskStatus.TODO);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));

        taskService.deleteTask(1L);

        verify(taskRepository).delete(existing);
    }

    @Test
    void shouldThrowWhenTaskIsMissing() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(99L))
            .isInstanceOf(TaskNotFoundException.class)
            .hasMessage("Task with id 99 was not found");
    }

    private TaskEntity taskEntity(Long id, TaskStatus status) {
        TaskEntity entity = new TaskEntity();
        entity.setId(id);
        entity.setTitle("Review file");
        entity.setDescription("Review the case file");
        entity.setStatus(status);
        entity.setDueDateTime(LocalDateTime.of(2026, 5, 10, 9, 0));
        entity.setCreatedAt(LocalDateTime.of(2026, 5, 1, 9, 0));
        entity.setUpdatedAt(LocalDateTime.of(2026, 5, 1, 9, 30));
        return entity;
    }
}
