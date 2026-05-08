package uk.gov.hmcts.dts.taskmanager.service;

import java.util.List;

import uk.gov.hmcts.dts.taskmanager.dto.TaskCreateRequest;
import uk.gov.hmcts.dts.taskmanager.dto.TaskResponse;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;

public interface TaskDataGateway {

    TaskResponse createTask(TaskCreateRequest request);

    TaskResponse getTaskById(Long id);

    List<TaskResponse> getAllTasks();

    TaskResponse updateTaskStatus(Long id, TaskStatus status);

    void deleteTask(Long id);
}
