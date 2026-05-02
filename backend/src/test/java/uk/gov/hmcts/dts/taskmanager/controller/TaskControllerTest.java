package uk.gov.hmcts.dts.taskmanager.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import uk.gov.hmcts.dts.taskmanager.config.OpenApiConfig;
import uk.gov.hmcts.dts.taskmanager.dto.TaskResponse;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;
import uk.gov.hmcts.dts.taskmanager.exception.GlobalExceptionHandler;
import uk.gov.hmcts.dts.taskmanager.exception.TaskNotFoundException;
import uk.gov.hmcts.dts.taskmanager.service.TaskService;

@WebMvcTest(TaskController.class)
@Import({GlobalExceptionHandler.class, OpenApiConfig.class})
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldCreateTask() throws Exception {
        when(taskService.createTask(org.mockito.ArgumentMatchers.any()))
            .thenReturn(taskResponse(1L, TaskStatus.TODO));

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "title": "Review file",
                      "description": "Review the case file",
                      "status": "TODO",
                      "dueDateTime": "2026-05-10T09:00:00"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void shouldReturnAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of(taskResponse(1L, TaskStatus.TODO)));

        mockMvc.perform(get("/api/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Review file"));
    }

    @Test
    void shouldReturnTaskById() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(taskResponse(1L, TaskStatus.IN_PROGRESS));

        mockMvc.perform(get("/api/tasks/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void shouldUpdateTaskStatus() throws Exception {
        when(taskService.updateTaskStatus(1L, TaskStatus.COMPLETED)).thenReturn(taskResponse(1L, TaskStatus.COMPLETED));

        mockMvc.perform(patch("/api/tasks/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "status": "COMPLETED"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnValidationErrors() throws Exception {
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "title": "",
                      "status": null,
                      "dueDateTime": null
                    }
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.validationErrors.length()").value(3));
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        when(taskService.getTaskById(42L)).thenThrow(new TaskNotFoundException(42L));

        mockMvc.perform(get("/api/tasks/42"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Task with id 42 was not found"));
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