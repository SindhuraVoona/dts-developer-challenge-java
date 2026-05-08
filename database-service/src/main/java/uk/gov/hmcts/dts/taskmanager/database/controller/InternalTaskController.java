package uk.gov.hmcts.dts.taskmanager.database.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import uk.gov.hmcts.dts.taskmanager.database.dto.TaskCreateRequest;
import uk.gov.hmcts.dts.taskmanager.database.dto.TaskResponse;
import uk.gov.hmcts.dts.taskmanager.database.dto.TaskStatusUpdateRequest;
import uk.gov.hmcts.dts.taskmanager.database.service.TaskPersistenceService;

@RestController
@RequestMapping("/internal/tasks")
public class InternalTaskController {

    private final TaskPersistenceService taskPersistenceService;

    public InternalTaskController(TaskPersistenceService taskPersistenceService) {
        this.taskPersistenceService = taskPersistenceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody TaskCreateRequest request) {
        return taskPersistenceService.createTask(request);
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskPersistenceService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskPersistenceService.getTaskById(id);
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateTaskStatus(@PathVariable Long id,
                                         @Valid @RequestBody TaskStatusUpdateRequest request) {
        return taskPersistenceService.updateTaskStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskPersistenceService.deleteTask(id);
    }
}
