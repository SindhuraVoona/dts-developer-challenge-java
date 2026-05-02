package uk.gov.hmcts.dts.taskmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import uk.gov.hmcts.dts.taskmanager.dto.TaskCreateRequest;
import uk.gov.hmcts.dts.taskmanager.dto.TaskResponse;
import uk.gov.hmcts.dts.taskmanager.entity.TaskEntity;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;
import uk.gov.hmcts.dts.taskmanager.exception.TaskNotFoundException;
import uk.gov.hmcts.dts.taskmanager.mapper.TaskMapper;
import uk.gov.hmcts.dts.taskmanager.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponse createTask(TaskCreateRequest request) {
        TaskEntity savedTask = taskRepository.save(taskMapper.toEntity(request));
        return taskMapper.toResponse(savedTask);
    }

    public TaskResponse getTaskById(Long id) {
        return taskMapper.toResponse(findTask(id));
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
            .stream()
            .map(taskMapper::toResponse)
            .toList();
    }

    public TaskResponse updateTaskStatus(Long id, TaskStatus status) {
        TaskEntity task = findTask(id);
        task.setStatus(status);
        TaskEntity savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    public void deleteTask(Long id) {
        TaskEntity task = findTask(id);
        taskRepository.delete(task);
    }

    private TaskEntity findTask(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
