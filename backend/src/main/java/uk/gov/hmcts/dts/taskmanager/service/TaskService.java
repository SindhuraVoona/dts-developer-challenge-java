package uk.gov.hmcts.dts.taskmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import uk.gov.hmcts.dts.taskmanager.dto.TaskCreateRequest;
import uk.gov.hmcts.dts.taskmanager.dto.TaskResponse;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;

@Service
public class TaskService {

    private final TaskDataGateway taskDataGateway;

    public TaskService(TaskDataGateway taskDataGateway) {
        this.taskDataGateway = taskDataGateway;
    }

    public TaskResponse createTask(TaskCreateRequest request) {
        return taskDataGateway.createTask(request);
    }

    public TaskResponse getTaskById(Long id) {
        return taskDataGateway.getTaskById(id);
    }

    public List<TaskResponse> getAllTasks() {
        return taskDataGateway.getAllTasks();
    }

    public TaskResponse updateTaskStatus(Long id, TaskStatus status) {
        return taskDataGateway.updateTaskStatus(id, status);
    }

    public void deleteTask(Long id) {
        taskDataGateway.deleteTask(id);
    }
}
